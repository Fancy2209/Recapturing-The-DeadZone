package dev.deadzone.socket.handler.save.bounty

import dev.deadzone.context.ServerContext
import dev.deadzone.socket.handler.save.SaveSubHandler
import dev.deadzone.socket.messaging.SaveDataMethod

class BountySaveHandler : SaveSubHandler {
    override suspend fun handle(
        type: String,
        saveId: String,
        data: Map<String, Any>,
        playerId: String,
        serverContext: ServerContext
    ) {
        when (type) {
            SaveDataMethod.BOUNTY_VIEW -> {}
            SaveDataMethod.BOUNTY_SPEED_UP -> {}
            SaveDataMethod.BOUNTY_NEW -> {}
            SaveDataMethod.BOUNTY_ABANDON -> {}
            SaveDataMethod.BOUNTY_ADD -> {}
        }
    }
}
