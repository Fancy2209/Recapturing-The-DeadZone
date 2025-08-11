package dev.deadzone.socket.handler.save

import dev.deadzone.context.ServerContext

interface SaveSubHandler {
    val supportedTypes: Set<String>

    suspend fun handle(
        type: String,
        saveId: String,
        data: Map<String, Any?>,
        playerId: String,
        send: suspend (ByteArray) -> Unit,
        serverContext: ServerContext
    )
}
