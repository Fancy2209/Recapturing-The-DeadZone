package dev.deadzone.core.utils

interface SocketMessageHandler {
    fun match(message: SocketMessage): Boolean
    fun handle(message: SocketMessage): ByteArray
}
