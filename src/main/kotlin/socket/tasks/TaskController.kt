package dev.deadzone.socket.tasks

interface TaskController {
    fun runTask(key: String)
    fun stopTask(key: String)
    fun addTaskCompletionCallback(key: String, cb: () -> Unit)
}