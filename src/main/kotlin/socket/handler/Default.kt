package dev.deadzone.socket.handler

import dev.deadzone.core.utils.PIOSerializer
import dev.deadzone.module.LogConfigSocketError
import dev.deadzone.socket.utils.SocketMessage
import dev.deadzone.socket.utils.SocketMessageHandler
import dev.deadzone.module.Logger
import dev.deadzone.socket.Connection

class DefaultHandler() : SocketMessageHandler {
    override fun match(message: SocketMessage): Boolean {
        return true
    }

    override suspend fun handle(
        connection: Connection,
        message: SocketMessage,
        send: suspend (ByteArray) -> Unit
    ) {
        Logger.error(LogConfigSocketError) { "Handler not registered or implemented for message: $message" }
        send(PIOSerializer.serialize(listOf("\u0000\u0000\u0000\u0000")))
    }
}
