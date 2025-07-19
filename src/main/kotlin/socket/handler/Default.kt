package dev.deadzone.socket.handler

import dev.deadzone.core.utils.PIOSerializer
import dev.deadzone.core.utils.SocketMessage
import dev.deadzone.core.utils.SocketMessageHandler

class DefaultHandler() : SocketMessageHandler {
    override fun match(message: SocketMessage): Boolean {
        return true
    }

    override suspend fun handle(
        message: SocketMessage,
        send: suspend (ByteArray) -> Unit
    ) {
        println("[SOCKET]: DEFAULT HANDLER NOT IMPLEMENTED")
        send(PIOSerializer.serialize(listOf("\u0000\u0000\u0000\u0000")))
    }
}
