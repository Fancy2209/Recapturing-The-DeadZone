package dev.deadzone.socket.handler

import dev.deadzone.core.BigDB
import dev.deadzone.core.utils.SocketMessage
import dev.deadzone.core.utils.SocketMessageHandler

/**
 * Handle `ic` message by:
 *
 * 1. IC
 *
 */
class InitComplete(private val db: BigDB) : SocketMessageHandler {
    override fun match(message: SocketMessage): Boolean {
        return message.getString("ic") != null
    }

    override suspend fun handle(
        message: SocketMessage,
        send: suspend (ByteArray) -> Unit
    ) {

    }
}
