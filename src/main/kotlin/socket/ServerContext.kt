package dev.deadzone.socket

import dev.deadzone.core.data.BigDB

data class ServerContext(
    val db: BigDB,
    val runTask: (String) -> Unit,
    val stopTask: (String) -> Unit,
    val addTaskCompletionCallback: (String, () -> Unit) -> Unit
)
