package dev.deadzone.core.utils

class SocketMessageDispatcher() {
    private val handlers = mutableListOf<SocketMessageHandler>()

    fun register(handler: SocketMessageHandler) {
        handlers.add(handler)
    }

    fun dispatch(msg: SocketMessage): ByteArray? {
        handlers.firstOrNull { it.match(msg) }?.let { handler ->
            println("Dispatched $msg to $handler")
            return handler.handle(msg)
        }
        return null
    }
}
