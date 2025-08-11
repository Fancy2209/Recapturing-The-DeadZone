package dev.deadzone.socket.handler.save.compound.misc

import dev.deadzone.context.ServerContext
import dev.deadzone.socket.handler.save.SaveSubHandler
import dev.deadzone.socket.messaging.SaveDataMethod

class CmpMiscSaveHandler : SaveSubHandler {
    override suspend fun handle(
        type: String,
        saveId: String,
        data: Map<String, Any>,
        playerId: String,
        serverContext: ServerContext
    ) {
        when (type) {
            SaveDataMethod.CRAFT_ITEM -> {}
            SaveDataMethod.CRAFT_UPGRADE -> {}
            SaveDataMethod.CRAFT_SCHEMATIC -> {}
            SaveDataMethod.EFFECT_SET -> {}
            SaveDataMethod.RESEARCH_START -> {}
            SaveDataMethod.AH_EVENT -> {}
            SaveDataMethod.CULL_NEIGHBORS -> {}
            SaveDataMethod.RALLY_ASSIGNMENT -> {}
        }
    }
}
