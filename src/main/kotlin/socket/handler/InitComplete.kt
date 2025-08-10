package dev.deadzone.socket.handler

import dev.deadzone.context.ServerContext
import dev.deadzone.module.LogSource
import dev.deadzone.module.Logger
import dev.deadzone.socket.core.Connection
import dev.deadzone.socket.tasks.TaskController
import dev.deadzone.socket.messaging.SocketMessage
import dev.deadzone.socket.messaging.SocketMessageHandler

/**
 * Handle `ic` message by:
 *
 * 1. Do the necessary setup in server.
 */
class InitCompleteHandler(
    private val context: ServerContext,
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
        context.onlinePlayerRegistry.markOnline(connection.playerId)

        // Create context for the player
        context.playerContextTracker.createContext(
            playerId = connection.playerId,
            connection = connection,
            db = context.db,
            useMongo = context.config.useMongo
        )

        // periodically send time update to client
        taskController.runTask("tu")
        taskController.addTaskCompletionCallback("tu") {
            Logger.info(LogSource.SOCKET) { "tu completed from ic" }
        }
    }
}
