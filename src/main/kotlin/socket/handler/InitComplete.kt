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
        // Client part sends network INIT_COMPLETE message, with no handler attached
        // not sure the purpose of that or what it expects the server to do
    }
}
