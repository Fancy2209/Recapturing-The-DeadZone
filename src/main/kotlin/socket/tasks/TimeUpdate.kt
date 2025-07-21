package dev.deadzone.socket.tasks

import dev.deadzone.socket.Connection
import dev.deadzone.socket.utils.ServerPushTask
import io.ktor.util.date.getTimeMillis
import kotlinx.coroutines.delay

class TimeUpdate: ServerPushTask {
    override val key: String
        get() = "tu"

    override suspend fun run(connection: Connection) {
        while (true) {
            delay(5000)
            connection.sendMessage(key, getTimeMillis())
        }
    }
}
