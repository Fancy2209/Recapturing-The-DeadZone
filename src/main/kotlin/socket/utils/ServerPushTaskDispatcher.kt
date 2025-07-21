package dev.deadzone.socket.utils

import dev.deadzone.module.Logger
import dev.deadzone.socket.Connection
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlin.coroutines.cancellation.CancellationException

/**
 * Manages and dispatches registered [ServerPushTask]s for a given socket connection.
 *
 * This is used to modularly register tasks that the server runs independently
 * to push messages to connected clients (e.g., time update, real-time events).
 *
 * @property tasks keep tracks registered tasks
 * @property runningTasks List of registered tasks key which are currently run.
 * Each has reference to coroutine job for cancellation.
 * @property taskSignals List of pending tasks which are waiting to be started.
 */
class ServerPushTaskDispatcher {
    private val tasks = mutableListOf<ServerPushTask>()
    private val runningTasks = mutableMapOf<String, Job>()
    private val taskSignals = mutableMapOf<String, CompletableDeferred<Unit>>()
    private val completionListeners = mutableMapOf<String, MutableList<() -> Unit>>()

    fun register(task: ServerPushTask) {
        tasks.add(task)
    }

    fun signalTaskReady(taskKey: String) {
        taskSignals.remove(taskKey)?.complete(Unit)
    }

    fun signalTaskStop(taskKey: String) {
        runningTasks.remove(taskKey)?.cancel()
    }

    fun runReadyTasks(connection: Connection, scope: CoroutineScope) {
        tasks.forEach { task ->
            if (runningTasks.containsKey(task.key)) return@forEach

            val job = scope.launch {
                val signal = CompletableDeferred<Unit>()
                taskSignals[task.key] = signal
                signal.await()
                Logger.socketPrint("Push task ${task.key} is ready to run.")

                try {
                    task.run(connection)
                    Logger.socketPrint("Push task ${task.key} ran successfully.")
                } catch (_: CancellationException) {
                    Logger.socketPrint("Push task '${task.key}' was cancelled.")
                } catch (e: Exception) {
                    Logger.socketPrint("Error running push task '${task.key}': ${e}")
                } finally {
                    runningTasks.remove(task.key)
                    notifyCompletion(task.key)
                }
            }

            runningTasks[task.key] = job
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
}
