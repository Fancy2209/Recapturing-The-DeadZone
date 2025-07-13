package dev.deadzone.core.utils.message.handler

import dev.deadzone.core.utils.PIOSerializer
import dev.deadzone.core.utils.SocketMessage
import dev.deadzone.core.utils.SocketMessageHandler

/**
 * Handle `join` message by:
 *
 * 1. Sending `playerio.joinresult`
 * 2. Sending `gr` message
 *
 */
class JoinHandler : SocketMessageHandler {
    override fun match(message: SocketMessage): Boolean {
        return message.getString("join") != null
    }

    override fun handle(message: SocketMessage): ByteArray {
        val joinKey = message.getString("join")
        println("Handling join with key: $joinKey")

        val msg = listOf("playerio.joinresult", true)

        return PIOSerializer.serialize(msg)
    }
}
