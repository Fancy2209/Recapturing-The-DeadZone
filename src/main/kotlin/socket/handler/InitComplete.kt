package dev.deadzone.socket.handler

import dev.deadzone.module.Logger
import dev.deadzone.socket.utils.SocketMessage
import dev.deadzone.socket.utils.SocketMessageHandler
import dev.deadzone.socket.Connection
import dev.deadzone.socket.ServerContext

/**
 * Handle `ic` message by:
 *
 * 1. IC
 *
 */
class InitCompleteHandler(private val context: ServerContext) : SocketMessageHandler {
    override fun match(message: SocketMessage): Boolean {
        // IC message is null, so only check for "ic" present
        return message.contains("ic")
    }

    override suspend fun handle(
        connection: Connection,
        message: SocketMessage,
        send: suspend (ByteArray) -> Unit
    ) {
        // Client part sends network INIT_COMPLETE message, with no handler attached
        // not sure the purpose of that or what it expects the server to do

        // When game init is completed, periodically send time update to client
        context.runTask("tu")
        context.addCompletionCallback("tu") {
            Logger.socketPrint("tu completed from ic")
        }
    }
}
