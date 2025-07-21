package dev.deadzone.socket

import dev.deadzone.core.BigDB
import dev.deadzone.socket.utils.ServerPushTaskDispatcher
import dev.deadzone.socket.utils.SocketMessageDispatcher

data class ServerContext(
    val socketMessageDispatcher: SocketMessageDispatcher,
    val serverPushTaskDispatcher: ServerPushTaskDispatcher,
    val db: BigDB,
)
