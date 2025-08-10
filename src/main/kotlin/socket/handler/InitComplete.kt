package dev.deadzone.socket.handler

import dev.deadzone.context.ServerContext
import dev.deadzone.socket.core.Connection
import dev.deadzone.socket.tasks.TaskController
import dev.deadzone.socket.messaging.SocketMessage
import dev.deadzone.socket.messaging.SocketMessageHandler
import dev.deadzone.utils.LogSource
import dev.deadzone.utils.Logger

/**
 * Handle `ic` message by:
 *
 * 1. Do the necessary setup in server.
 */
class InitCompleteHandler(
    private val serverContext: ServerContext,
    private val taskController: TaskController
) :
    SocketMessageHandler {
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
        // Likely only signal to server

        // When game init is completed, mark player as active
        serverContext.onlinePlayerRegistry.markOnline(connection.playerId)

        // periodically send time update to client
        taskController.runTask("tu")
        taskController.addTaskCompletionCallback("tu") {
            Logger.info(LogSource.SOCKET) { "tu completed from ic" }
        }
    }
}
