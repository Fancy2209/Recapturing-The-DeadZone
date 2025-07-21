package dev.deadzone.socket.utils

import dev.deadzone.socket.Connection

/**
 * A template for socket message handler
 *
 * See example
 * - [dev.deadzone.socket.handler.JoinHandler]
 * - [dev.deadzone.socket.handler.DefaultHandler]
 */
interface SocketMessageHandler {
    fun match(message: SocketMessage): Boolean
    suspend fun handle(connection: Connection, message: SocketMessage, send: suspend (ByteArray) -> Unit)
}
