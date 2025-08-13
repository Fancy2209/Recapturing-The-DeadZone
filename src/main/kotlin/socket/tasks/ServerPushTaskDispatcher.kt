package dev.deadzone.socket.tasks

import dev.deadzone.socket.core.Connection
import dev.deadzone.utils.LogConfigSocketError
import dev.deadzone.utils.LogSource
import dev.deadzone.utils.Logger
import kotlinx.coroutines.*
import kotlin.coroutines.cancellation.CancellationException
import kotlin.coroutines.coroutineContext

/**
 * Manages and dispatches registered [ServerPushTask]s for a given socket connection.
 *
 * This is used to modularly register tasks that the server runs independently
 * to push messages to connected clients (e.g., time update, real-time events).
 *
 * @property tasks keep tracks registered tasks
 * @property configs config for each task. This will always override the default config
 * with the given on each [runTask].
 * @property runningTasks List of registered tasks key which are currently run.
 * Each has reference to coroutine job for cancellation.
 * @property taskSignals List of pending tasks which are waiting to be started.
 */
class ServerPushTaskDispatcher : TaskScheduler {
    private val tasks = mutableListOf<ServerPushTask>()
    private val configs = mutableMapOf<String, TaskConfig>()
    private val runningTasks = mutableMapOf<String, Job>()
    private val taskSignals = mutableMapOf<String, CompletableDeferred<Unit>>()
    private val completionListeners = mutableMapOf<String, MutableList<() -> Unit>>()

    fun register(task: ServerPushTask) {
        tasks.add(task)
        // register the default config
        this.configs[task.key] = task.config
    }

    fun runTask(taskKey: String, configBuilder: (TaskConfig) -> TaskConfig?) {
        val defaultConfig = requireNotNull(configs[taskKey]) { "Missing task config for taskKey=$taskKey" }
        val config = configBuilder(defaultConfig) ?: defaultConfig
        configs[taskKey] = config
        taskSignals.remove(taskKey)?.complete(Unit)
    }

    fun stopTask(taskKey: String) {
        runningTasks.remove(taskKey)?.cancel()
    }

    fun runReadyTasks(connection: Connection, scope: CoroutineScope) {
        tasks.forEach { task ->
            if (runningTasks.containsKey(task.key)) return@forEach

            val job = scope.launch {
                val signal = CompletableDeferred<Unit>()
                taskSignals[task.key] = signal
                signal.await()

                Logger.info(LogSource.SOCKET) { "Push task ${task.key} is ready to run." }

                try {
                    val scheduler = task.scheduler ?: this@ServerPushTaskDispatcher
                    scheduler.schedule(task, connection)
                    Logger.info(LogSource.SOCKET) { "Push task ${task.key} ran successfully." }
                } catch (_: CancellationException) {
                    Logger.info(LogSource.SOCKET) { "Push task '${task.key}' was cancelled." }
                } catch (e: Exception) {
                    Logger.error(LogConfigSocketError) { "Error running push task '${task.key}': $e" }
                } finally {
                    runningTasks.remove(task.key)
                    notifyCompletion(task.key)
                }
            }

            runningTasks[task.key] = job
        }
    }

    override suspend fun schedule(
        task: ServerPushTask,
        connection: Connection
    ) {
        val finalConfig = configs[task.key]!!
        delay(finalConfig.initialRunDelay)

        val shouldRunInfinitely = finalConfig.repeatDelay != null

        if (shouldRunInfinitely) {
            while (coroutineContext.isActive) {
                delay(finalConfig.repeatDelay)
                task.run(connection)
            }
        } else {
            task.run(connection)
        }
    }

    fun stopAllPushTasks() {
        runningTasks.values.forEach { it.cancel() }
        runningTasks.clear()
    }

    fun addCompletionListener(key: String, listener: () -> Unit) {
        completionListeners.getOrPut(key) { mutableListOf() }.add(listener)
    }

    private fun notifyCompletion(key: String) {
        completionListeners.remove(key)?.forEach { it.invoke() }
    }

    fun shutdown() {
        runningTasks.values.forEach { it.cancel() }
        taskSignals.values.forEach { signal -> if (!signal.isCompleted) signal.complete(Unit) }

        runningTasks.clear()
        taskSignals.clear()
        completionListeners.clear()
        tasks.clear()
    }
}
