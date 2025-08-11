package dev.deadzone.socket.handler.save.crate

import dev.deadzone.context.ServerContext
import dev.deadzone.socket.handler.save.SaveSubHandler
import dev.deadzone.socket.messaging.SaveDataMethod

class CrateSaveHandler : SaveSubHandler {
    override suspend fun handle(
        type: String,
        saveId: String,
        data: Map<String, Any>,
        playerId: String,
        serverContext: ServerContext
    ) {
        when (type) {
            SaveDataMethod.CRATE_UNLOCK -> {}
            SaveDataMethod.CRATE_MYSTERY_UNLOCK -> {}
        }
    }
}