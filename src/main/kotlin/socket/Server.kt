package dev.deadzone.socket

import io.ktor.network.selector.*
import io.ktor.network.sockets.*
import io.ktor.utils.io.*
import kotlinx.coroutines.*
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
                while (true) {
                    val data = readUntilNull(input)
                    print("Received: $data")

                    when {
                        data.contains(POLICY_FILE_REQUEST) -> {
                            output.writeFully(POLICY_FILE_RESPONSE.toByteArray(Charsets.UTF_8))
                            output.flush()
                            print("Sent policy response")
                        }

                        data == "\u0000" -> {
                            print("Ignoring null byte")
                        }

                        data.contains("join") -> {
                            print("Join request received: $data")
                        }

                        else -> {
                            print("Unimplemented data: $data")
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

    private suspend fun readUntilNull(input: ByteReadChannel): String {
        val bytes = mutableListOf<Byte>()
        while (!input.isClosedForRead) {
            val b = input.readByte()
            if (b == 0.toByte()) break
            bytes.add(b)
        }
        return bytes.toByteArray().toString(Charsets.UTF_8)
    }

    fun stop() {
        print("Stopping ${clients.size} connections...")
        clients.forEach {
            it.socket.close()
        }
        print("Server closed.")
    }

}

fun Server.print(msg: Any) {
    println("[SOCKET]: $msg")
}
