package dev.deadzone.socket

import dev.deadzone.core.BigDB
import dev.deadzone.socket.utils.ServerPushTaskDispatcher
import dev.deadzone.socket.utils.SocketMessageDispatcher

data class ServerContext(
    val db: BigDB,
    val runTask: (String) -> Unit,
    val stopTask: (String) -> Unit,
    val addCompletionCallback: (String, () -> Unit) -> Unit
)
