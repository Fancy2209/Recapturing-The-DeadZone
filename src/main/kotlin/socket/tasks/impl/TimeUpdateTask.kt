package dev.deadzone.socket.tasks.impl

import dev.deadzone.socket.core.Connection
import dev.deadzone.context.ServerContext
import dev.deadzone.socket.messaging.NetworkMessage
import dev.deadzone.socket.tasks.ServerPushTask
import io.ktor.util.date.getTimeMillis
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlin.coroutines.coroutineContext

/**
 * Sends a time update ('tu') message to client.
 *
 * The game registers callback for such message, though not sure how frequent should we send the message.
 */
class TimeUpdateTask(serverContext: ServerContext): ServerPushTask {
    override val key: String
        get() = NetworkMessage.TIME_UPDATE

    override suspend fun run(connection: Connection) {
        while (coroutineContext.isActive) {
            delay(1000)
            connection.sendMessage(key, getTimeMillis())
        }
    }
}
