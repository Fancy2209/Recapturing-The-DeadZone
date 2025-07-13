package dev.deadzone.core.utils

class MessageDispatcher() {
    private val handlers = mutableListOf<MessageHandler>()

    fun register(handler: MessageHandler) {
        handlers.add(handler)
    }

    fun dispatch(msg: Message): ByteArray? {
        handlers.firstOrNull { it.match(msg) }?.let { handler ->
            println("Dispatched $msg to $handler")
            return handler.handle(msg)
        }
        return null
    }
}
