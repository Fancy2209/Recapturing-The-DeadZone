package dev.deadzone.socket

import dev.deadzone.core.auth.AuthProvider
import dev.deadzone.core.auth.SessionManager
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

data class ServerContext(
    val db: BigDB,
    val sessionManager: SessionManager,
    val playerRegistry: PlayerRegistry,
    val authProvider: AuthProvider,
)

class Server(
    private val host: String = "127.0.0.1",
    private val port: Int = 7777,
    private val context: ServerContext,
    private val coroutineScope: CoroutineScope = CoroutineScope(Dispatchers.IO + SupervisorJob()),
): TaskController {
    private val clients = Collections.synchronizedList(mutableListOf<Connection>())
    private val socketDispatcher = SocketMessageDispatcher()
    private val taskDispatcher = ServerPushTaskDispatcher()

    init {
        with(context) {
            socketDispatcher.register(JoinHandler(this))
            socketDispatcher.register(QuestProgressHandler(this))
            socketDispatcher.register(InitCompleteHandler(this, this@Server))
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
                Logger.info { "Socket server started at $host:$port" }

                while (true) {
                    val socket = serverSocket.accept()

                    val connection = Connection(
                        connectionId = UUID.randomUUID().toString(),
                        socket = socket,
                        output = socket.openWriteChannel(autoFlush = true)
                    )
                    clients.add(connection)
                    Logger.info { "New client: ${connection.socket.remoteAddress}" }
                    handleClient(connection)
                }
            } catch (e: Exception) {
                Logger.error { "ERROR starting server $e" }
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
                    Logger.debug { "Received raw: ${data.decodeToString()}" }

                    if (data.startsWithBytes(POLICY_FILE_REQUEST.toByteArray())) {
                        connection.sendRaw(POLICY_FILE_RESPONSE.toByteArray())
                        Logger.info { "Policy file request received and sent" }
                        break
                    }

                    val data2 = if (data.startsWithBytes(byteArrayOf(0x00))) {
                        Logger.info { "Received 0x00 — ignoring" }
                        data.drop(1).toByteArray()
                    } else data

                    val deserialized = PIODeserializer.deserialize(data2)
                    val msg = SocketMessage.fromRaw(deserialized)
                    if (msg.isEmpty()) continue

                    socketDispatcher.findHandlerFor(msg).let { handler ->
                        handler.handle(connection, msg) { response ->
                            // Each handler serialize the message, so sendRaw directly
                            connection.sendRaw(response)
                        }
                    }

                    Logger.info("<------------ SOCKET MESSAGE END ------------>")
                }
            } catch (e: Exception) {
                Logger.error { "Error in socket for ${connection.socket.remoteAddress}: $e" }
                connection.playerId?.let {
                    context.playerRegistry.markOffline(it)
                }
            } finally {
                Logger.info { "Client ${connection.socket.remoteAddress} disconnected" }
                connection.playerId?.let {
                    context.playerRegistry.markOffline(it)
                }
                taskDispatcher.stopAllPushTasks()
                pushJob.cancelAndJoin()
                clients.remove(connection)
                connection.socket.close()
            }
        }
    }

    fun stop() {
        Logger.info { "Stopping ${clients.size} connections..." }
        clients.forEach {
            it.socket.close()
        }
        context.playerRegistry.shutdown()
        Logger.info { "Server closed." }
    }

    override fun runTask(key: String) {
        taskDispatcher.signalTaskReady(key)
    }

    override fun stopTask(key: String) {
        taskDispatcher.signalTaskStop(key)
    }

    override fun addTaskCompletionCallback(key: String, cb: () -> Unit) {
        taskDispatcher.addCompletionListener(key, cb)
    }
}

fun ByteArray.startsWithBytes(prefix: ByteArray): Boolean {
    if (this.size < prefix.size) return false
    for (i in prefix.indices) {
        if (this[i] != prefix[i]) return false
    }
    return true
}
