package dev.deadzone.socket.utils

import dev.deadzone.socket.handler.DefaultHandler

/**
 * Dispatch [SocketMessage] to a registered handler
 */
class SocketMessageDispatcher() {
    private val handlers = mutableListOf<SocketMessageHandler>()

    fun register(handler: SocketMessageHandler) {
        handlers.add(handler)
    }

    fun findHandlerFor(msg: SocketMessage): SocketMessageHandler {
        return handlers.find { it.match(msg) } ?: DefaultHandler()
    }
}

