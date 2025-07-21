package dev.deadzone.socket.utils

import dev.deadzone.module.Logger
import dev.deadzone.socket.Connection

/**
 * Manages and dispatches registered [ServerPushTask]s for a given socket connection.
 *
 * This is used to modularly register tasks that the server runs independently
 * to push messages to connected clients (e.g., time update, real-time events).
 *
 */
class ServerPushTaskDispatcher {
    private val tasks = mutableListOf<ServerPushTask>()

    fun register(task: ServerPushTask) {
        tasks.add(task)
    }

    /**
     * Run ready tasks registered from [Connection.signalTaskReady]
     */
    suspend fun runReadyTasks(connection: Connection) {
        tasks.forEach { task ->
            connection.awaitTaskReady(task.key)
            Logger.socketPrint("${task.key} is ready to run.")

            try {
                task.run(connection)
                Logger.socketPrint("${task.key} ran successfully.")
            } catch (e: Exception) {
                Logger.socketPrint("Error running push task '${task.key}': ${e}")
            }
        }
    }
}
