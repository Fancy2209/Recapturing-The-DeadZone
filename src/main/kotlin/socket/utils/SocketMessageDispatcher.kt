package dev.deadzone.socket.utils

import dev.deadzone.socket.handler.DefaultHandler
import dev.deadzone.socket.print

/**
 * Dispatch [SocketMessage] to a registered handler
 */
class SocketMessageDispatcher() {
    private val handlers = mutableListOf<SocketMessageHandler>()

    fun register(handler: SocketMessageHandler) {
        handlers.add(handler)
    }

    fun findHandlerFor(msg: SocketMessage): SocketMessageHandler {
        print("Finding handler for message: $msg")
        return (handlers.find { it.match(msg) } ?: DefaultHandler()).also {
            print("Dispatching to $it")
        }
    }
}
