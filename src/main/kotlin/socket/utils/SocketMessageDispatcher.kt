package dev.deadzone.socket.utils

import dev.deadzone.module.Logger
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
        Logger.info { "Finding handler for type: ${msg.type} | message: $msg" }
        return (handlers.find { it.match(msg) } ?: DefaultHandler()).also {
            Logger.info {"Dispatching to $it" }
        }
    }
}
