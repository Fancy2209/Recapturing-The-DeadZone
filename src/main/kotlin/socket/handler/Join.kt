package dev.deadzone.core.utils.message.handler

import dev.deadzone.core.utils.Message
import dev.deadzone.core.utils.MessageHandler
import dev.deadzone.core.utils.PIOSerializer

/**
 * Handle `join` message by:
 *
 * 1. Sending `playerio.joinresult`
 * 2. Sending `gr` message
 *
 */
class JoinHandler : MessageHandler {
    override fun match(message: Message): Boolean {
        return message.getString("join") != null
    }

    override fun handle(message: Message): ByteArray {
        val joinKey = message.getString("join")
        println("Handling join with key: $joinKey")

        val msg = listOf("playerio.joinresult", true)

        return PIOSerializer.serialize(msg)
    }
}
