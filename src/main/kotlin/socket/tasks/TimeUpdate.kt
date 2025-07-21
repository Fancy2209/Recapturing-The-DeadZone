package dev.deadzone.socket.tasks

import dev.deadzone.socket.Connection
import dev.deadzone.socket.ServerContext
import dev.deadzone.socket.utils.ServerPushTask
import io.ktor.util.date.getTimeMillis
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlin.coroutines.coroutineContext

class TimeUpdate(context: ServerContext): ServerPushTask {
    override val key: String
        get() = "tu"

    override suspend fun run(connection: Connection) {
        while (coroutineContext.isActive) {
            delay(5000)
            connection.sendMessage(key, getTimeMillis())
        }
    }
}
