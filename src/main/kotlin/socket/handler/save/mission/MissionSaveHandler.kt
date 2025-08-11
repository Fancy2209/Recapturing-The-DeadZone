package dev.deadzone.socket.handler.save.mission

import dev.deadzone.context.ServerContext
import dev.deadzone.socket.handler.save.SaveSubHandler
import dev.deadzone.socket.messaging.SaveDataMethod

class MissionSaveHandler : SaveSubHandler {
    override suspend fun handle(
        type: String,
        saveId: String,
        data: Map<String, Any>,
        playerId: String,
        serverContext: ServerContext
    ) {
        when (type) {
            SaveDataMethod.MISSION_START -> {}
            SaveDataMethod.MISSION_END -> {}
            SaveDataMethod.MISSION_INJURY -> {}
            SaveDataMethod.MISSION_SPEED_UP -> {}
            SaveDataMethod.MISSION_ZOMBIES -> {}
            SaveDataMethod.MISSION_START_FLAG -> {}
            SaveDataMethod.MISSION_INTERACTION_FLAG -> {}
            SaveDataMethod.MISSION_SCOUTED -> {}
            SaveDataMethod.MISSION_ITEM_USE -> {}
            SaveDataMethod.MISSION_TRIGGER -> {}
            SaveDataMethod.MISSION_ELITE_SPAWNED -> {}
            SaveDataMethod.MISSION_ELITE_KILLED -> {}

            // also handle this
            SaveDataMethod.STAT_DATA -> {}
        }
    }
}
