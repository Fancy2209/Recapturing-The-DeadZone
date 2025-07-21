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
    private val tasks = mutableMapOf<String, ServerPushTask>()

    fun register(name: String, task: ServerPushTask) {
        tasks[name] = task
    }

    fun getTask(name: String): ServerPushTask? = tasks[name]

    suspend fun runSelected(connection: Connection, taskNames: List<String>) {
        taskNames.forEach { name ->
            val task = tasks[name]
            if (task != null) {
                try {
                    task.run(connection)
                } catch (e: Exception) {
                    Logger.socketPrint("Error running push task [$name]: $e")
                }
            } else {
                Logger.socketPrint("No push task registered with name: $name")
            }
        }
    }
}
