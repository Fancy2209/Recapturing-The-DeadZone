package dev.deadzone.core.utils

interface MessageHandler {
    fun match(message: Message): Boolean
    fun handle(message: Message): ByteArray
}
