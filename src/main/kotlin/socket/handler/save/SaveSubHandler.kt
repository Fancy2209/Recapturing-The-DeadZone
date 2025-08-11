package dev.deadzone.socket.handler.save

import dev.deadzone.context.ServerContext

interface SaveSubHandler {
    suspend fun handle(
        type: String,
        saveId: String,
        data: Map<String, Any>,
        playerId: String,
        serverContext: ServerContext,
    )
}
