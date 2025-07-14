package dev.deadzone.core.utils.message.handler

import dev.deadzone.core.BigDB
import dev.deadzone.core.utils.PIOSerializer
import dev.deadzone.core.utils.SocketMessage
import dev.deadzone.core.utils.SocketMessageHandler
import io.ktor.util.date.*
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
class JoinHandler(private val db: BigDB) : SocketMessageHandler {
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
            loadCostTable(),
            loadSrvTable(),
            loadPlayerLoginState()
        )
        send(PIOSerializer.serialize(gameReadyMsg))
    }

    /**
     * Pack all xml.gz resources in data/xml/ and manually added compressed
     * resources_secondary.xml.gz in data/
     */
    fun produceBinaries(): ByteArray {
        val xmlResources = listOf(
            "alliances.xml.gz",
            "arenas.xml.gz",
            "attire.xml.gz",
            "badwords.xml.gz",
            "buildings.xml.gz",
            "config.xml.gz",
            "crafting.xml.gz",
            "effects.xml.gz",
            "humanenemies.xml.gz",
            "injury.xml.gz",
            "itemmods.xml.gz",
            "items.xml.gz",
            "quests.xml.gz",
            "quests_global.xml.gz",
            "raids.xml.gz",
            "skills.xml.gz",
            "streetstructs.xml.gz",
            "survivor.xml.gz",
            "vehiclenames.xml.gz",
            "zombie.xml.gz",
            "resources_secondary.xml.gz"
        )

        val output = ByteArrayOutputStream()

        // 1. Write number of files as a single byte
        output.write(xmlResources.size)

        val classLoader = Thread.currentThread().contextClassLoader

        for (filename in xmlResources) {
            val path = if (filename == "resources_secondary.xml.gz")
                "static/game/data/$filename"
            else
                "static/game/data/xml/$filename"

            val inputStream = classLoader.getResourceAsStream(path)
                ?: throw IllegalStateException("File not found in resources: $path")
            val fileBytes = inputStream.readBytes()

            val uri = "xml/" + filename.removeSuffix(".gz")
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

    /**
     * Load cost table which is based on CostTable.as (currently still mocked)
     */
    fun loadCostTable(): String  {
        return loadRawJson("cost_table.json")
    }

    /**
     * Load survivor table which is based on Survivor.as (not exactly same)
     */
    fun loadSrvTable(): String  {
        return loadRawJson("srv_table.json")
    }

    /**
     * Load player login state which is based on Player's state from PlayerData.as
     * (currently still mocked and hardcoded, not from player's database)
     */
    fun loadPlayerLoginState(): String  {
        return loadRawJson("login_state.json")
    }

    fun loadRawJson(path: String): String  {
        return object {}.javaClass.getResource("/data/$path")?.readText()
            ?: error("Resource not found: $path")
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
