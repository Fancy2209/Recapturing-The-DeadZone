package dev.deadzone.socket.tasks

import dev.deadzone.socket.core.Connection
import dev.deadzone.context.ServerContext
import io.ktor.util.date.getTimeMillis
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlin.coroutines.coroutineContext

/**
 * Sends a time update ('tu') message to client.
 *
 * The game registers callback for such message, though not sure how frequent should we send the message.
 */
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
