package dev.deadzone.socket.handler

import dev.deadzone.core.utils.PIOSerializer
import dev.deadzone.socket.Connection
import dev.deadzone.socket.ServerContext
import dev.deadzone.socket.utils.SocketMessage
import dev.deadzone.socket.utils.SocketMessageHandler

/**
 * Handle `qp` message by:
 *
 * 1. Sending quest progress JSON
 *
 */
class QuestProgressHandler(private val context: ServerContext) : SocketMessageHandler {
    override fun match(message: SocketMessage): Boolean {
        return message.getString("qp") != null
    }

    override suspend fun handle(connection: Connection, message: SocketMessage, send: suspend (ByteArray) -> Unit) {
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
