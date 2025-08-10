package dev.deadzone.socket.core

import dev.deadzone.socket.protocol.PIOSerializer
import dev.deadzone.utils.Logger
import io.ktor.network.sockets.Socket
import io.ktor.utils.io.ByteWriteChannel
import io.ktor.utils.io.writeFully
import java.util.UUID

/**
 * Representation of a player connection.
 * @property playerId reference to which player does this socket belongs to. Only known after client send join message.
 */
class Connection(
    var playerId: String = "",
    val connectionId: String = UUID.randomUUID().toString(),
    val socket: Socket,
    private val output: ByteWriteChannel,
) {
    /**
     * Send raw unserialized message (non-PIO) to client
     *
     * @param b raw message in bytearray
     */
    suspend fun sendRaw(b: ByteArray, logFull: Boolean = false) {
        Logger.debug(logFull = logFull) { "Sending raw: ${b.decodeToString()}" }
        output.writeFully(b)
    }

    /**
     * Send a serialized PIO message
     *
     * @param type The type of the message (e.g., "gr", "ic")
     * @param args Any number of message content
     */
    suspend fun sendMessage(type: String, vararg args: Any, logFull: Boolean = false) {
        val msg = buildList {
            add(type)
            addAll(args)
        }
        val bytes = PIOSerializer.serialize(msg)

        Logger.debug(logFull = logFull) { "Sending message of type '$type' | raw message: ${bytes.decodeToString()}" }
        output.writeFully(bytes)
    }

    override fun toString(): String {
        return "[ADDR]: ${this.socket.remoteAddress} | connectionId=$connectionId"
    }
}
