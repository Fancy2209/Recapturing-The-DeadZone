package dev.deadzone.socket.handler.save

fun interface SaveSubHandler {
    suspend fun handle(message: String, data: Map<*, *>, playerId: String)
}