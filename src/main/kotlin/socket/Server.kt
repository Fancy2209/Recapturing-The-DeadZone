package dev.deadzone.socket

import io.ktor.network.selector.*
import io.ktor.network.sockets.*
import io.ktor.utils.io.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import java.util.Collections

const val POLICY_FILE_REQUEST = "<policy-file-request/>"
const val POLICY_FILE_RESPONSE = """
<cross-domain-policy>
<allow-access-from domain="*" to-ports="7777"/>
</cross-domain-policy>\x00
"""

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
            try {
                val socket = connection.socket
                val readChannel = socket.openReadChannel()
                val writeChannel = socket.openWriteChannel(autoFlush = true)

                while (true) {
                    val line = readChannel.readUTF8Line() ?: break
                    print("Received: $line")

                    print(clients.forEach { print(it.toString()) })
                    if (line.startsWith(POLICY_FILE_REQUEST)) {
                        writeChannel.writeStringUtf8(POLICY_FILE_RESPONSE.trimIndent())
                        print(POLICY_FILE_RESPONSE.trimIndent())
                    }

                    if (line.startsWith("join")) {
                        print("received join")
                    }
                    // dispatch...
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

    private fun stop() {
        print("Closing socket server... disconnecting (${clients.size}) clients.")
        clients.forEach {
            it.socket.close()
        }
        clients.clear()
        print("Socket server closed.")
    }
}

fun Server.print(msg: Any) {
    println("[SOCKET]: $msg")
}
