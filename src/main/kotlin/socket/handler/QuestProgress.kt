package dev.deadzone.socket.handler

import dev.deadzone.core.BigDB
import dev.deadzone.core.utils.PIOSerializer
import dev.deadzone.core.utils.SocketMessage
import dev.deadzone.core.utils.SocketMessageHandler

/**
 * Handle `join` message by:
 *
 * 1. Sending `qp` message
 *
 */
class QuestProgressHandler(private val db: BigDB) : SocketMessageHandler {
    override fun match(message: SocketMessage): Boolean {
        return message.getString("qp") != null
    }

    override suspend fun handle(message: SocketMessage, send: suspend (ByteArray) -> Unit) {
        val message = listOf("qp", questProgressJson.trimIndent())
        send(PIOSerializer.serialize(message))
    }
}

const val questProgressJson = """
{
  "complete": null,
  "progress": null
}
"""
