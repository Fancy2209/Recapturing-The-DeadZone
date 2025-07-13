package dev.deadzone.core.utils

class SocketMessageDispatcher() {
    private val handlers = mutableListOf<SocketMessageHandler>()

    fun register(handler: SocketMessageHandler) {
        handlers.add(handler)
    }

    fun findHandlerFor(msg: SocketMessage): SocketMessageHandler? {
        return handlers.firstOrNull { it.match(msg) }
    }
}
