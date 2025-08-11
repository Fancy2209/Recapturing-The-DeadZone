package dev.deadzone.socket.handler.save.compound.building

import dev.deadzone.context.ServerContext
import dev.deadzone.socket.handler.save.SaveSubHandler
import dev.deadzone.socket.messaging.SaveDataMethod

class BuildingSaveHandler : SaveSubHandler {
    override suspend fun handle(
        type: String,
        saveId: String,
        data: Map<String, Any>,
        playerId: String,
        serverContext: ServerContext
    ) {
        when (type) {
            SaveDataMethod.BUILDING_CREATE -> {}
            SaveDataMethod.BUILDING_CREATE_BUY -> {}
            SaveDataMethod.BUILDING_UPGRADE -> {}
            SaveDataMethod.BUILDING_UPGRADE_BUY -> {}
            SaveDataMethod.BUILDING_MOVE -> {}
            SaveDataMethod.BUILDING_RECYCLE -> {}
            SaveDataMethod.BUILDING_COLLECT -> {}
            SaveDataMethod.BUILDING_CANCEL -> {}
            SaveDataMethod.BUILDING_SPEED_UP -> {}
            SaveDataMethod.BUILDING_REPAIR -> {}
            SaveDataMethod.BUILDING_REPAIR_BUY -> {}
            SaveDataMethod.BUILDING_REPAIR_SPEED_UP -> {}
            SaveDataMethod.BUILDING_TRAP_EXPLODE -> {}
        }
    }
}
