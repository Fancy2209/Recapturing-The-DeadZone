package dev.deadzone.socket.handler

import dev.deadzone.core.utils.PIOSerializer
import dev.deadzone.core.utils.SocketMessage
import dev.deadzone.core.utils.SocketMessageHandler
import dev.deadzone.module.FileLogger

class DefaultHandler() : SocketMessageHandler {
    override fun match(message: SocketMessage): Boolean {
        return true
    }

    override suspend fun handle(
        message: SocketMessage,
        send: suspend (ByteArray) -> Unit
    ) {
        println("[SOCKET]: DEFAULT HANDLER NOT REGISTERED or IMPLEMENTED")
        FileLogger.unimplementedSocket(message)
        send(PIOSerializer.serialize(listOf("\u0000\u0000\u0000\u0000")))
    }
}
