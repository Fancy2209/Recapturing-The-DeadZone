package dev.deadzone.socket

import dev.deadzone.core.utils.PIOSerializer
import dev.deadzone.module.Logger
import io.ktor.network.sockets.*
import io.ktor.utils.io.*
import kotlin.collections.toByteArray

/**
 * Representation of a player connection.
 * @property playerId reference to which player does this socket belongs to. Only known after client send join message.
 */
class Connection(
    val socket: Socket,
    var playerId: String? = null,
    private val output: ByteWriteChannel,
) {

    /**
     * Send raw unserialized message (non-PIO) to client
     *
     * @param b raw message in bytearray
     */
    suspend fun sendRaw(b: ByteArray, printFull: Boolean = false) {
        val truncated = if (b.size > 500 && !printFull) {
            b.take(500).toByteArray().decodeToString() + "... [truncated]"
        } else {
            b.decodeToString()
        }

        Logger.socketPrint("Sending raw: $truncated")
        output.writeFully(b)
    }

    /**
     * Send a serialized PIO message
     *
     * @param type The type of the message (e.g., "gr", "ic")
     * @param args Any number of message content
     */
    suspend fun sendMessage(type: String, vararg args: Any, printFull: Boolean = false) {
        val msg = buildList {
            add(type)
            addAll(args)
        }
        val bytes = PIOSerializer.serialize(msg)

        val truncated = if (bytes.size > 500 && !printFull) {
            bytes.take(500).toByteArray().decodeToString() + "... [truncated]"
        } else {
            bytes.decodeToString()
        }

        Logger.socketPrint("Sending message of type '$type' | raw message: $truncated")
        output.writeFully(bytes)
    }

    override fun toString(): String {
        return "[ADDR]: ${this.socket.remoteAddress} | playerId: $playerId"
    }
}
