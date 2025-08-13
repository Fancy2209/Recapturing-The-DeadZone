package dev.deadzone.socket.tasks.impl

import dev.deadzone.context.ServerContext
import dev.deadzone.socket.core.Connection
import dev.deadzone.socket.messaging.NetworkMessage
import dev.deadzone.socket.tasks.ServerPushTask
import io.ktor.util.date.getTimeMillis
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlin.coroutines.coroutineContext

class BuildingUpgradeTask(serverContext: ServerContext): ServerPushTask {
    override val key: String
        get() = NetworkMessage.TASK_COMPLETE

    override suspend fun run(connection: Connection) {
        while (coroutineContext.isActive) {
            delay(11000)
            connection.sendMessage(key, getTimeMillis())
        }
    }
}
