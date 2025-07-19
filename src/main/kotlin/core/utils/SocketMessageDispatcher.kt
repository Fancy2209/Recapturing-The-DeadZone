package dev.deadzone.core.utils

import dev.deadzone.socket.handler.DefaultHandler

class SocketMessageDispatcher() {
    private val handlers = mutableListOf<SocketMessageHandler>()

    fun register(handler: SocketMessageHandler) {
        handlers.add(handler)
    }

    fun findHandlerFor(msg: SocketMessage): SocketMessageHandler {
        return handlers.find { it.match(msg) } ?: DefaultHandler()
    }
}

