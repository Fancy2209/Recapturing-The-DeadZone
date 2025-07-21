package dev.deadzone.socket.utils

import dev.deadzone.socket.Connection

interface SocketMessageHandler {
    fun match(message: SocketMessage): Boolean
    suspend fun handle(connection: Connection, message: SocketMessage, send: suspend (ByteArray) -> Unit)
}
