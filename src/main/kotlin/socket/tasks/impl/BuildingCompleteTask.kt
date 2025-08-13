package dev.deadzone.socket.tasks.impl

import dev.deadzone.context.ServerContext
import dev.deadzone.socket.core.Connection
import dev.deadzone.socket.messaging.NetworkMessage
import dev.deadzone.socket.tasks.ServerPushTask
import dev.deadzone.socket.tasks.TaskConfig
import dev.deadzone.socket.tasks.TaskScheduler
import kotlin.time.Duration.Companion.seconds

class BuildingCompleteTask(serverContext: ServerContext) : ServerPushTask {
    override val key: String
        get() = NetworkMessage.BUILDING_COMPLETE

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
        val customMessage = finalConfig.extra["msg"] as? List<Any?> ?: emptyList()
        connection.sendMessage(key, customMessage)
    }
}
