package dev.deadzone.core.utils

interface SocketMessageHandler {
    fun match(message: SocketMessage): Boolean
    suspend fun handle(message: SocketMessage, send: suspend (ByteArray) -> Unit)
}
