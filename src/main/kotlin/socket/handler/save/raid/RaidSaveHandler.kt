package dev.deadzone.socket.handler.save.raid

import dev.deadzone.context.ServerContext
import dev.deadzone.socket.handler.save.SaveSubHandler
import dev.deadzone.socket.messaging.SaveDataMethod

class RaidSaveHandler : SaveSubHandler {
    override suspend fun handle(
        type: String,
        saveId: String,
        data: Map<String, Any>,
        playerId: String,
        serverContext: ServerContext
    ) {
        when (type) {
            SaveDataMethod.RAID_START -> {}
            SaveDataMethod.RAID_CONTINUE -> {}
            SaveDataMethod.RAID_ABORT -> {}
            SaveDataMethod.RAID_DEATH -> {}
        }
    }
}
