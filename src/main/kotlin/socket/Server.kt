package dev.deadzone.socket

import dev.deadzone.core.BigDB
import dev.deadzone.core.utils.PIODeserializer
import dev.deadzone.socket.handler.InitCompleteHandler
import dev.deadzone.socket.utils.SocketMessage
import dev.deadzone.socket.utils.SocketMessageDispatcher
import dev.deadzone.socket.handler.JoinHandler
import dev.deadzone.socket.handler.QuestProgressHandler
import dev.deadzone.socket.handler.SaveHandler
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
    private val maxConnections: Int = 5,
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
            taskDispatcher.register(TimeUpdate(this))
        }
    }

    fun start() {
        coroutineScope.launch {
            try {
                val selectorManager = SelectorManager(Dispatchers.IO)
                val serverSocket = aSocket(selectorManager).tcp().bind(host, port)
                print("Socket server started at $host:$port")

                while (true) {
                    val socket = serverSocket.accept()

                    if (clients.size >= maxConnections) {
                        print("Maximum connections reached. Refusing connection from ${socket.remoteAddress}")
                        socket.close()
                        continue
                    }

                    val connection = Connection(
                        socket = socket,
                        output = socket.openWriteChannel(autoFlush = true)
                    )
                    clients.add(connection)
                    print("New client: ${connection.socket.remoteAddress}")
                    handleClient(connection)
                }
            } catch (e: Exception) {
                print("ERROR starting server $e")
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
                    print("Received raw: ${data.printString()}")

                    if (data.startsWithBytes(POLICY_FILE_REQUEST.toByteArray())) {
                        connection.sendRaw(POLICY_FILE_RESPONSE.toByteArray())
                        print("Sent policy response")
                        break
                    }

                    val data2 = if (data.startsWithBytes(byteArrayOf(0x00))) {
                        print("Received 0x00 --- ignoring")
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

                    print("============END=============")
                }
            } catch (e: Exception) {
                print("Error in socket for ${connection.socket.remoteAddress}: $e")
            } finally {
                print("Client ${connection.socket.remoteAddress} disconnected")
                taskDispatcher.stopAllPushTasks()
                pushJob.cancelAndJoin()
                clients.remove(connection)
                connection.socket.close()
            }
        }
    }

    fun stop() {
        print("Stopping ${clients.size} connections...")
        clients.forEach {
            it.socket.close()
        }
        print("Server closed.")
    }
}

private const val MAX_PRINT_STRING_LENGTH = 300

fun ByteArray.printString(printFull: Boolean = false): String {
    val builder = StringBuilder()
    var count = 0
    for (byte in this) {
        if (count >= MAX_PRINT_STRING_LENGTH && !printFull) {
            builder.append("... [truncated]")
            break
        }
        val b = byte.toInt() and 0xFF
        builder.append(
            if (b in 0x20..0x7E) b.toChar()
            else "\\x%02x".format(b)
        )
        count++
    }
    return builder.toString()
}

fun ByteArray.startsWithBytes(prefix: ByteArray): Boolean {
    if (this.size < prefix.size) return false
    for (i in prefix.indices) {
        if (this[i] != prefix[i]) return false
    }
    return true
}

fun print(msg: Any, printFull: Boolean = false) {
    val str = msg.toString()
    val limited = if (str.length > MAX_PRINT_STRING_LENGTH && !printFull)
        str.take(MAX_PRINT_STRING_LENGTH) + "... [truncated]"
    else
        str

    println("[SOCKET]: $limited")
}
