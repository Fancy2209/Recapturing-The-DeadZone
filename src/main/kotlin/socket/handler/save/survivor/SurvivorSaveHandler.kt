package dev.deadzone.socket.handler.save.survivor

import dev.deadzone.context.ServerContext
import dev.deadzone.socket.handler.save.SaveSubHandler
import dev.deadzone.socket.messaging.SaveDataMethod

class SurvivorSaveHandler : SaveSubHandler {
    override suspend fun handle(
        type: String,
        saveId: String,
        data: Map<String, Any>,
        playerId: String,
        serverContext: ServerContext
    ) {
        when (type) {
            SaveDataMethod.SURVIVOR_CLASS -> {}
            SaveDataMethod.SURVIVOR_OFFENCE_LOADOUT -> {}
            SaveDataMethod.SURVIVOR_DEFENCE_LOADOUT -> {}
            SaveDataMethod.SURVIVOR_CLOTHING_LOADOUT -> {}
            SaveDataMethod.SURVIVOR_INJURY_SPEED_UP -> {}
            SaveDataMethod.SURVIVOR_RENAME -> {}
            SaveDataMethod.SURVIVOR_REASSIGN -> {}
            SaveDataMethod.SURVIVOR_REASSIGN_SPEED_UP -> {}
            SaveDataMethod.SURVIVOR_BUY -> {}
            SaveDataMethod.SURVIVOR_INJURE -> {}
            SaveDataMethod.SURVIVOR_ENEMY_INJURE -> {}
            SaveDataMethod.SURVIVOR_HEAL_INJURY -> {}
            SaveDataMethod.SURVIVOR_HEAL_ALL -> {}
            SaveDataMethod.PLAYER_CUSTOM -> {}
            SaveDataMethod.SURVIVOR_EDIT -> {}
            SaveDataMethod.NAMES -> {}
            SaveDataMethod.RESET_LEADER -> {}
        }
    }
}
