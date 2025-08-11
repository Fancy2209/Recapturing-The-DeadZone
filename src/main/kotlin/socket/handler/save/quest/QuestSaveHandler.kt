package dev.deadzone.socket.handler.save.quest

import dev.deadzone.context.ServerContext
import dev.deadzone.socket.handler.save.SaveSubHandler
import dev.deadzone.socket.messaging.SaveDataMethod

class QuestSaveHandler : SaveSubHandler {
    override suspend fun handle(
        type: String,
        saveId: String,
        data: Map<String, Any>,
        playerId: String,
        serverContext: ServerContext
    ) {
        when (type) {
            SaveDataMethod.QUEST_COLLECT -> {}
            SaveDataMethod.QUEST_TRACK -> {}
            SaveDataMethod.QUEST_UNTRACK -> {}
            SaveDataMethod.QUEST_DAILY_DECLINE -> {}
            SaveDataMethod.QUEST_DAILY_ACCEPT -> {}
            SaveDataMethod.REPEAT_ACHIEVEMENT -> {}
            SaveDataMethod.GLOBAL_QUEST_COLLECT -> {}
        }
    }
}
