package dev.deadzone.socket.handler

import dev.deadzone.context.GlobalContext
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
        Logger.debug { "Received RSC of saveId: ${message.getString("id")}" }

        val messageToSend =
            listOf(
                NetworkMessage.REQUEST_SURVIVOR_CHECK,
                GlobalContext.json.encodeToString(mapOf("success" to true))
            )

        send(PIOSerializer.serialize(messageToSend))
    }
}
