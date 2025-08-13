package dev.deadzone.socket.tasks

import dev.deadzone.socket.core.Connection

/**
 * A template of task which can be run flexibly
 *
 * Implementations of this interface can push data to clients through client's [Connection]
 * if needed.
 */
interface ServerTask {
    /**
     * Identifier for the task.
     */
    val key: String

    /**
     * Default config for the task.
     */
    val config: TaskConfig

    /**
     * A scheduler override from the default [ServerTaskDispatcher].
     */
    val scheduler: TaskScheduler?

    /**
     * Run the task. Task do not need to schedule its running as scheduling is done by [TaskScheduler]
     *
     * @param connection the player's socket connection to send message if needed.
     */
    suspend fun run(connection: Connection, finalConfig: TaskConfig)
}
