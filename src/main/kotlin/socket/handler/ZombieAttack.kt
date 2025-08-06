package dev.deadzone.socket.handler

import dev.deadzone.utils.PIOSerializer
import dev.deadzone.socket.Connection
import dev.deadzone.socket.ServerContext
import dev.deadzone.socket.utils.SocketMessage
import dev.deadzone.socket.utils.SocketMessageHandler

class ZombieAttackHandler(private val context: ServerContext): SocketMessageHandler {
    override fun match(message: SocketMessage): Boolean {
        return message.contains("rza")
    }

    override suspend fun handle(
        connection: Connection,
        message: SocketMessage,
        send: suspend (ByteArray) -> Unit
    ) {
        val message = listOf("za")
        send(PIOSerializer.serialize(message))
    }
}
