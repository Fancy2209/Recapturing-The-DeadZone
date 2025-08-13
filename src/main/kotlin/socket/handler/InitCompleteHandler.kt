package dev.deadzone.socket.handler

import dev.deadzone.context.ServerContext
import dev.deadzone.socket.core.Connection
import dev.deadzone.socket.messaging.NetworkMessage
import dev.deadzone.socket.messaging.SocketMessage
import dev.deadzone.socket.messaging.SocketMessageHandler
import dev.deadzone.socket.tasks.TaskTemplate

/**
 * Handle `ic` message by:
 *
 * 1. Do the necessary setup in server.
 */
class InitCompleteHandler(private val serverContext: ServerContext) :
    SocketMessageHandler {
    override fun match(message: SocketMessage): Boolean {
        // IC message is null, so only check for "ic" present
        return message.contains(NetworkMessage.INIT_COMPLETE)
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

        // send serverTime to client (required)
        serverContext.taskDispatcher.runTask(
            connection = connection,
            taskTemplateKey = TaskTemplate.TIME_UPDATE,
            cfgBuilder = { null },
            onComplete = {}
        )
    }
}
