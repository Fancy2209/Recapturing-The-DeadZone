package dev.deadzone.socket.handler

import dev.deadzone.socket.protocol.PIOSerializer
import dev.deadzone.socket.core.Connection
import dev.deadzone.context.ServerContext
import dev.deadzone.socket.messaging.SocketMessage
import dev.deadzone.socket.messaging.SocketMessageHandler

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
