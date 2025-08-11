package dev.deadzone.socket.handler.save.arena

import dev.deadzone.context.ServerContext
import dev.deadzone.socket.handler.save.SaveSubHandler
import dev.deadzone.socket.messaging.SaveDataMethod

class ArenaSaveHandler : SaveSubHandler {
    override suspend fun handle(
        type: String,
        saveId: String,
        data: Map<String, Any>,
        playerId: String,
        serverContext: ServerContext
    ) {
        when (type) {
            SaveDataMethod.ARENA_START -> {}
            SaveDataMethod.ARENA_CONTINUE -> {}
            SaveDataMethod.ARENA_FINISH -> {}
            SaveDataMethod.ARENA_ABORT -> {}
            SaveDataMethod.ARENA_DEATH -> {}
            SaveDataMethod.ARENA_UPDATE -> {}
            SaveDataMethod.ARENA_LEADER -> {}
            SaveDataMethod.ARENA_LEADERBOARD -> {}
        }
    }
}
