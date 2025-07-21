package dev.deadzone.socket

import dev.deadzone.core.utils.PIOSerializer
import io.ktor.network.sockets.Socket
import io.ktor.utils.io.ByteWriteChannel
import io.ktor.utils.io.writeFully
import kotlinx.coroutines.CompletableDeferred

/**
 * Representation of a player connection.
 * @property playerId reference to which player does this socket belongs to. Only known after client send join message.
 */
class Connection(
    val socket: Socket,
    var playerId: String? = null,
    private val output: ByteWriteChannel,
) {
    private val readyTasks = mutableSetOf<String>()
    private val taskSignals = mutableMapOf<String, CompletableDeferred<Unit>>()

    /**
     * Send raw unserialized message (non-PIO) to client
     *
     * @param b raw message in bytearray
     */
    suspend fun sendRaw(b: ByteArray) {
        print("Sending ${b.printString()}")
        output.writeFully(b)
    }

    /**
     * Send a serialized PIO message
     *
     * @param type The type of the message (e.g., "gr", "ic")
     * @param args Any number of message content
     */
    suspend fun sendMessage(type: String, vararg args: Any) {
        val msg = buildList {
            add(type)
            addAll(args)
        }
        val bytes = PIOSerializer.serialize(msg)
        print("Sending ${bytes.printString()}")
        output.writeFully(bytes)
    }

    /**
     * Signal that a task is ready to be started now
     */
    fun signalTaskReady(taskKey: String) {
        if (!readyTasks.contains(taskKey)) {
            readyTasks.add(taskKey)
            taskSignals[taskKey]?.complete(Unit)
        }
    }

    suspend fun awaitTaskReady(taskKey: String) {
        if (readyTasks.contains(taskKey)) return
        val signal = CompletableDeferred<Unit>()
        taskSignals[taskKey] = signal
        signal.await()
    }

    override fun toString(): String {
        return "[ADDR]: ${this.socket.remoteAddress} | playerId: $playerId"
    }
}
