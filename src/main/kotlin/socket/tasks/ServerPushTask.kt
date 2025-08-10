package dev.deadzone.socket.tasks

import dev.deadzone.socket.core.Connection

/**
 * Represents a server-initiated task that can periodically or conditionally
 * run in the server.
 *
 * Implementations of this interface can push data to clients through client's [Connection]
 * if needed.
 */
interface ServerPushTask {
    val key: String
    suspend fun run(connection: Connection)
}
