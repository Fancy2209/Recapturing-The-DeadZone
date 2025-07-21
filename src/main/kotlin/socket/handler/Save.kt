package dev.deadzone.socket.handler

import dev.deadzone.core.BigDB
import dev.deadzone.socket.utils.SocketMessage
import dev.deadzone.socket.utils.SocketMessageHandler
import dev.deadzone.module.Logger
import dev.deadzone.socket.Connection

/**
 * Handle `save` message by:
 *
 * 1. Save
 *
 *
 * Save message is allegedly one-way signal from client to server.
 * Client send save message and expects server to save it, that's it.
 *
 */
class SaveHandler(private val db: BigDB) : SocketMessageHandler {
    override fun match(message: SocketMessage): Boolean {
        return message.getString("s") != null
    }

    override suspend fun handle(
        connection: Connection,
        message: SocketMessage,
        send: suspend (ByteArray) -> Unit
    ) {
        val data = message.getMap("data")
        val type = data?.get("_type") as? String
        val id = data?.get("id") as? String

        Logger.socketPrint("Save message got: type:$type | id:$id")

        when (type) {
            "get_offers" -> {

            }

            "chat_getContactsBlocks" -> {

            }

            else -> {
                Logger.unimplementedSocket("Handled 's' message but unhandled data type: $type from data=$data")
            }
        }
    }
}
