package dev.deadzone.socket.handler.save.compound.task

import dev.deadzone.context.ServerContext
import dev.deadzone.socket.handler.save.SaveSubHandler
import dev.deadzone.socket.messaging.SaveDataMethod

class TaskSaveHandler : SaveSubHandler {
    override suspend fun handle(
        type: String,
        saveId: String,
        data: Map<String, Any>,
        playerId: String,
        serverContext: ServerContext
    ) {
        when (type) {
            SaveDataMethod.TASK_STARTED -> {}
            SaveDataMethod.TASK_CANCELLED -> {}
            SaveDataMethod.TASK_SURVIVOR_ASSIGNED -> {}
            SaveDataMethod.TASK_SURVIVOR_REMOVED -> {}
            SaveDataMethod.TASK_SPEED_UP -> {}
        }
    }
}
