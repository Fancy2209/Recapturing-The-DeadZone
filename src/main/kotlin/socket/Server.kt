package dev.deadzone.socket

import io.ktor.network.selector.*
import io.ktor.network.sockets.*
import io.ktor.utils.io.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

class Server(
    private val host: String = "127.0.0.1",
    private val port: Int = 7777,
    private val coroutineScope: CoroutineScope = CoroutineScope(Dispatchers.IO + SupervisorJob()),
) {
    private val clients: List<Connection> = mutableListOf()

    fun start() {
        coroutineScope.launch {
            val selectorManager = SelectorManager(Dispatchers.IO)
            val serverSocket = aSocket(selectorManager).tcp().bind(host, port)
            println("Socket server started at $host:$port")

            while (true) {
                val connection = Connection(socket = serverSocket.accept())

                println("New client: $connection")

                handleClient(connection)
            }
        }
    }

    private fun handleClient(connection: Connection) {
        coroutineScope.launch {
            val socket = connection.socket
            val readChannel = socket.openReadChannel()
            val writeChannel = socket.openWriteChannel(autoFlush = true)

            try {
                while (true) {
                    val line = readChannel.readUTF8Line() ?: break
                    println("[SOCKET] Received: $line")

                    // dispatch...
                }
            } catch (e: Exception) {
                println("Error with client ${socket.remoteAddress}: ${e.message}")
            } finally {
                println("Client ${socket.remoteAddress} disconnected")
                socket.close()
            }
        }
    }

    private fun stop() {
        println("Closing socket server... disconnecting (${clients.size}) clients.")
        clients.forEach {
            it.socket.close()
        }
        println("Socket server closed.")
    }
}
