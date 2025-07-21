package dev.deadzone.socket.utils

import dev.deadzone.socket.Connection

/**
 * Represents a server-initiated task that can periodically or conditionally
 * send messages to a connected client via the socket server.
 *
 * Implementations of this interface define logic to push data to clients
 * without requiring a client-initiated message.
 */
interface ServerPushTask {
    suspend fun run(connection: Connection)
}
