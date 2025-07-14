package dev.deadzone.core.utils.message.handler

import dev.deadzone.config.GamePaths
import dev.deadzone.core.utils.PIOSerializer
import dev.deadzone.core.utils.SocketMessage
import dev.deadzone.core.utils.SocketMessageHandler
import io.ktor.util.date.getTimeMillis
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.buildJsonObject
import org.jetbrains.exposed.sql.Database
import java.io.ByteArrayOutputStream
import java.nio.ByteBuffer
import java.nio.ByteOrder

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

        // Second message: game ready message
        val gameReadyMsg = listOf(
            "gr",
            getTimeMillis(),
            produceBinaries(),
            buildJsonObject { },
            buildJsonObject { },
            buildJsonObject { }
        )
        send(PIOSerializer.serialize(gameReadyMsg))
    }

    fun produceBinaries(): ByteArray {
        // Get all xml resources and also include resources_secondary
        val xmlResources = GamePaths.XML_DIR.listFiles().filter { file ->
            file.extension == "gz"
        } + listOf(GamePaths.BASE_PATH.resolve("resources_secondary_xml"))


        val output = ByteArrayOutputStream()

        // 1. Write number of files as a single byte
        output.write(xmlResources.size)

        for (file in xmlResources) {
            val fileBytes = file.readBytes()

            val name = file.name.removeSuffix(".gz")
            val uri = "xml/$name"
            val uriBytes = uri.toByteArray(Charsets.UTF_8)

            // 2. Write URI length as 2-byte little endian
            output.writeShortLE(uriBytes.size)

            // 3. Write URI bytes
            output.write(uriBytes)

            // 4. Write file size as 4-byte little endian
            output.writeIntLE(fileBytes.size)

            // 5. Write file data
            output.write(fileBytes)
        }

        return output.toByteArray()
    }
}

fun ByteArrayOutputStream.writeShortLE(value: Int) {
    val buf = ByteBuffer.allocate(2).order(ByteOrder.LITTLE_ENDIAN).putShort(value.toShort())
    write(buf.array())
}

fun ByteArrayOutputStream.writeIntLE(value: Int) {
    val buf = ByteBuffer.allocate(4).order(ByteOrder.LITTLE_ENDIAN).putInt(value)
    write(buf.array())
}
