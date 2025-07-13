package dev.deadzone.core.utils

class MessageDispatcher() {
    private val handlers = mutableListOf<MessageHandler>()

    fun register(handler: MessageHandler) {
        handlers.add(handler)
    }

    fun dispatch(msg: Message) {
        handlers.firstOrNull { it.match(msg) }?.handle(msg)
    }
}
