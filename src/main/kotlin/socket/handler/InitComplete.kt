package dev.deadzone.socket.handler

import dev.deadzone.core.PlayerServiceLocator
import dev.deadzone.core.survivor.SurvivorRepository
import dev.deadzone.core.survivor.SurvivorRepositoryMongo
import dev.deadzone.core.survivor.SurvivorService
import dev.deadzone.module.LogSource
import dev.deadzone.module.Logger
import dev.deadzone.socket.utils.SocketMessage
import dev.deadzone.socket.utils.SocketMessageHandler
import dev.deadzone.socket.Connection
import dev.deadzone.socket.ServerContext
import dev.deadzone.socket.TaskController

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
        connection.playerId?.let { context.playerRegistry.markOnline(it) }

        // periodically send time update to client
        taskController.runTask("tu")
        taskController.addTaskCompletionCallback("tu") {
            Logger.info(LogSource.SOCKET) { "tu completed from ic" }
        }

        // register factory for game services
        if (context.config.useMongo) {
            val udocs = context.db.getUserDocumentCollection()

            PlayerServiceLocator.registerFactory(SurvivorService::class) {
                val repo = SurvivorRepositoryMongo(udocs)
                SurvivorService(repo)
            }
        } else {
            // use other db implementation...
        }
    }
}
