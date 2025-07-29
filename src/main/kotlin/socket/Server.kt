package dev.deadzone.socket

import dev.deadzone.core.data.BigDB
import dev.deadzone.core.utils.PIODeserializer
import dev.deadzone.module.Logger
import dev.deadzone.socket.handler.InitCompleteHandler
import dev.deadzone.socket.utils.SocketMessage
import dev.deadzone.socket.utils.SocketMessageDispatcher
import dev.deadzone.socket.handler.JoinHandler
import dev.deadzone.socket.handler.QuestProgressHandler
import dev.deadzone.socket.handler.SaveHandler
import dev.deadzone.socket.handler.ZombieAttackHandler
import dev.deadzone.socket.tasks.TimeUpdate
import dev.deadzone.socket.utils.ServerPushTaskDispatcher
import io.ktor.network.selector.*
import io.ktor.network.sockets.*
import io.ktor.utils.io.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.launch
import java.util.*

const val POLICY_FILE_REQUEST = "<policy-file-request/>"
const val POLICY_FILE_RESPONSE =
    "<cross-domain-policy><allow-access-from domain=\"*\" to-ports=\"7777\"/></cross-domain-policy>\u0000"

class Server(
    private val host: String = "127.0.0.1",
    private val port: Int = 7777,
    private val db: BigDB,
    private val coroutineScope: CoroutineScope = CoroutineScope(Dispatchers.IO + SupervisorJob()),
) {
    private val clients = Collections.synchronizedList(mutableListOf<Connection>())
    private val socketDispatcher = SocketMessageDispatcher()
    private val taskDispatcher = ServerPushTaskDispatcher()
    private val context = ServerContext(
        db = db,
        runTask = { key -> taskDispatcher.signalTaskReady(key) },
        stopTask = { key -> taskDispatcher.signalTaskStop(key) },
        addTaskCompletionCallback = { key, cb -> taskDispatcher.addCompletionListener(key, cb) }
    )

    init {
        with(context) {
            socketDispatcher.register(JoinHandler(this))
            socketDispatcher.register(QuestProgressHandler(this))
            socketDispatcher.register(InitCompleteHandler(this))
            socketDispatcher.register(SaveHandler(this))
            socketDispatcher.register(ZombieAttackHandler(this))
            taskDispatcher.register(TimeUpdate(this))
        }
    }

    fun start() {
        coroutineScope.launch {
            try {
                val selectorManager = SelectorManager(Dispatchers.IO)
                val serverSocket = aSocket(selectorManager).tcp().bind(host, port)
                Logger.socketPrint("Socket server started at $host:$port")

                while (true) {
                    val socket = serverSocket.accept()

                    val connection = Connection(
                        socket = socket,
                        output = socket.openWriteChannel(autoFlush = true)
                    )
                    clients.add(connection)
                    Logger.socketPrint("New client: ${connection.socket.remoteAddress}")
                    handleClient(connection)
                }
            } catch (e: Exception) {
                Logger.socketPrint("ERROR starting server $e")
                stop()
            }
        }
    }

    private fun handleClient(connection: Connection) {
        coroutineScope.launch {
            val socket = connection.socket
            val input = socket.openReadChannel()

            val pushJob = coroutineScope.launch {
                taskDispatcher.runReadyTasks(connection, this)
            }

            try {
                val buffer = ByteArray(4096)

                while (true) {
                    val bytesRead = input.readAvailable(buffer, 0, buffer.size)
                    if (bytesRead <= 0) break

                    val data = buffer.copyOfRange(0, bytesRead)
                    Logger.socketPrint("Received raw: ${data.decodeToString()}")

                    if (data.startsWithBytes(POLICY_FILE_REQUEST.toByteArray())) {
                        connection.sendRaw(POLICY_FILE_RESPONSE.toByteArray())
                        Logger.socketPrint("Policy file request received and sent")
                        break
                    }

                    val data2 = if (data.startsWithBytes(byteArrayOf(0x00))) {
                        Logger.socketPrint("Received 0x00 — ignoring")
                        data.drop(1).toByteArray()
                    } else data

                    val deserialized = PIODeserializer.deserialize(data2)
                    val msg = SocketMessage.fromRaw(deserialized)

                    socketDispatcher.findHandlerFor(msg).let { handler ->
                        handler.handle(connection, msg) { response ->
                            // Each handler serialize the message, so sendRaw directly
                            connection.sendRaw(response)
                        }
                    }

                    Logger.socketPrint("<------------ END ------------>")
                }
            } catch (e: Exception) {
                Logger.socketPrint("Error in socket for ${connection.socket.remoteAddress}: $e")
            } finally {
                Logger.socketPrint("Client ${connection.socket.remoteAddress} disconnected")
                taskDispatcher.stopAllPushTasks()
                pushJob.cancelAndJoin()
                clients.remove(connection)
                connection.socket.close()
            }
        }
    }

    fun stop() {
        Logger.socketPrint("Stopping ${clients.size} connections...")
        clients.forEach {
            it.socket.close()
        }
        Logger.socketPrint("Server closed.")
    }
}

fun ByteArray.startsWithBytes(prefix: ByteArray): Boolean {
    if (this.size < prefix.size) return false
    for (i in prefix.indices) {
        if (this[i] != prefix[i]) return false
    }
    return true
}
