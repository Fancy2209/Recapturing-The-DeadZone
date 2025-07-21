package dev.deadzone.socket.handler

import dev.deadzone.core.utils.PIOSerializer
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
        Logger.socketPrint("DEFAULT HANDLER NOT REGISTERED or IMPLEMENTED")
        Logger.unimplementedSocket(message)
        send(PIOSerializer.serialize(listOf("\u0000\u0000\u0000\u0000")))
    }
}
