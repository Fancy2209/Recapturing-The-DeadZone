package dev.deadzone.socket.tasks

import dev.deadzone.socket.Connection
import dev.deadzone.socket.utils.ServerPushTask
import io.ktor.util.date.getTimeMillis
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlin.coroutines.coroutineContext

class TimeUpdate: ServerPushTask {
    override val key: String
        get() = "tu"

    @Volatile
    private var _shouldRun: Boolean = true

    override var shouldRun: Boolean
        get() = _shouldRun
        set(value) { _shouldRun = value }

    override suspend fun run(connection: Connection) {
        while (coroutineContext.isActive) {
            if (!shouldRun) break
            delay(5000)
            connection.sendMessage(key, getTimeMillis())
        }
    }
}
