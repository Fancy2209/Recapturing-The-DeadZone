package dev.deadzone.core.utils.message.handler

import dev.deadzone.core.utils.PIOSerializer
import dev.deadzone.core.utils.SocketMessage
import dev.deadzone.core.utils.SocketMessageHandler
import io.ktor.util.date.getTimeMillis
import kotlinx.serialization.json.Json
import org.jetbrains.exposed.sql.Database

/**
 * Handle `join` message by:
 *
 * 1. Sending `playerio.joinresult`
 * 2. Sending `gr` message
 *
 */
class JoinHandler(private val db: Database) : SocketMessageHandler {
    override fun match(message: SocketMessage): Boolean {
        return message.getString("join") != null
    }

    override suspend fun handle(message: SocketMessage, send: suspend (ByteArray) -> Unit) {
        val joinKey = message.getString("join")
        println("Handling join with key: $joinKey")

        // First message: join result
        val joinResultMsg = listOf("playerio.joinresult", true)
        send(PIOSerializer.serialize(joinResultMsg))

        // Optional: Do some async work here (e.g. DB or delay)
        // delay(100)

        // Second message: game ready message
        val gameReadyMsg = listOf(
            "gr",
            getTimeMillis(),
            byteArrayOf(),
            Json.encodeToString(""),
            Json.encodeToString(""),
            Json.encodeToString("")
        )
        send(PIOSerializer.serialize(gameReadyMsg))
    }
}
