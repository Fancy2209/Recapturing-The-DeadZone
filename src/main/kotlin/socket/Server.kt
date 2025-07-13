package dev.deadzone.socket

import dev.deadzone.core.utils.PIODeserializer
import dev.deadzone.core.utils.SocketMessage
import dev.deadzone.core.utils.SocketMessageDispatcher
import dev.deadzone.core.utils.message.handler.JoinHandler
import io.ktor.network.selector.*
import io.ktor.network.sockets.*
import io.ktor.utils.io.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import java.util.*

const val POLICY_FILE_REQUEST = "<policy-file-request/>"
const val POLICY_FILE_RESPONSE =
    "<cross-domain-policy><allow-access-from domain=\"*\" to-ports=\"7777\"/></cross-domain-policy>\u0000"

class Server(
    private val host: String = "127.0.0.1",
    private val port: Int = 7777,
    private val maxConnections: Int = 5,
    private val coroutineScope: CoroutineScope = CoroutineScope(Dispatchers.IO + SupervisorJob()),
) {
    private val clients = Collections.synchronizedList(mutableListOf<Connection>())
    val dispatcher = SocketMessageDispatcher().apply {
        register(JoinHandler())
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

                    val connection = Connection(socket = socket)
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
            val output = socket.openWriteChannel(autoFlush = true)

            try {
                val buffer = ByteArray(4096)

                while (true) {
                    val bytesRead = input.readAvailable(buffer, 0, buffer.size)
                    if (bytesRead <= 0) break

                    val data = buffer.copyOfRange(0, bytesRead)
                    print("Received raw: ${data.printString()}")

                    if (data.startsWithBytes(POLICY_FILE_REQUEST.toByteArray())) {
                        output.writeFully(POLICY_FILE_RESPONSE.toByteArray())
                        print("Sent policy response")
                        break
                    }

                    val data2 = if (data.startsWithBytes(byteArrayOf(0x00))) {
                        print("Received 0x00 --- ignoring")
                        data.drop(1).toByteArray()
                    } else data

                    val deserialized = PIODeserializer.deserialize(data2)
                    val msg = SocketMessage.fromRaw(deserialized)

                    dispatcher.findHandlerFor(msg)?.let { handler ->
                        print("Got msg: $msg")
                        print("Dispatching to $handler")
                        handler.handle(msg) { response ->
                            output.writeFully(response)
                        }
                    }
                }
            } catch (e: Exception) {
                print("Error with client ${connection.socket.remoteAddress}: ${e.message}")
            } finally {
                print("Client ${connection.socket.remoteAddress} disconnected")
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

fun ByteArray.printString(): String {
    return this.joinToString("") { byte ->
        val b = byte.toInt() and 0xFF
        if (b in 0x20..0x7E) b.toChar().toString()
        else "\\x%02x".format(b)
    }
}

fun ByteArray.startsWithBytes(prefix: ByteArray): Boolean {
    if (this.size < prefix.size) return false
    for (i in prefix.indices) {
        if (this[i] != prefix[i]) return false
    }
    return true
}

fun print(msg: Any) {
    println("[SOCKET]: $msg")
}
