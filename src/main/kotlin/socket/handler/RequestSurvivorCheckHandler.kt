package dev.deadzone.socket.handler

import dev.deadzone.socket.core.Connection
import dev.deadzone.socket.messaging.NetworkMessage
import dev.deadzone.socket.messaging.SocketMessage
import dev.deadzone.socket.messaging.SocketMessageHandler
import dev.deadzone.socket.protocol.PIOSerializer
import dev.deadzone.utils.Logger

class RequestSurvivorCheckHandler() : SocketMessageHandler {
    override fun match(message: SocketMessage): Boolean {
        return message.type == NetworkMessage.REQUEST_SURVIVOR_CHECK || message.contains(NetworkMessage.REQUEST_SURVIVOR_CHECK)
    }

    override suspend fun handle(
        connection: Connection,
        message: SocketMessage,
        send: suspend (ByteArray) -> Unit
    ) {
        val id = message.getMap("rsc")?.get("id") as String?
        Logger.debug { "Received RSC of saveId: $id" }

        val messageToSend =
            listOf(
                NetworkMessage.REQUEST_SURVIVOR_CHECK,
            )

        send(PIOSerializer.serialize(messageToSend))
    }
}
