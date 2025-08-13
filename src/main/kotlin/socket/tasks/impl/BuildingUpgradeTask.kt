package dev.deadzone.socket.tasks.impl

import dev.deadzone.context.ServerContext
import dev.deadzone.socket.core.Connection
import dev.deadzone.socket.messaging.NetworkMessage
import dev.deadzone.socket.tasks.ServerPushTask
import dev.deadzone.socket.tasks.TaskConfig
import dev.deadzone.socket.tasks.TaskScheduler
import kotlin.time.Duration.Companion.seconds

class BuildingUpgradeTask(serverContext: ServerContext) : ServerPushTask {
    override val key: String
        get() = NetworkMessage.TASK_COMPLETE

    override val config: TaskConfig
        get() = TaskConfig(
            // each building task should have initial run delay, which is when building upgrade is finished
            initialRunDelay = 0.seconds,
            repeatDelay = null,
            extra = emptyMap(),
        )

    override val scheduler: TaskScheduler?
        get() = null

    override suspend fun run(connection: Connection, finalConfig: TaskConfig) {
        val taskId =
            requireNotNull(finalConfig.extra["taskId"] as String?) { "Missing taskId when running BuildingUpgradeTask for playerId=${connection.playerId}" }

        connection.sendMessage(NetworkMessage.TASK_COMPLETE, taskId)
    }
}
