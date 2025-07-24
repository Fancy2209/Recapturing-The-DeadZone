package dev.deadzone.socket.handler

import dev.deadzone.core.model.game.data.GameResources
import dev.deadzone.core.model.game.data.skills.SkillCollection
import dev.deadzone.core.model.game.data.skills.SkillState
import dev.deadzone.core.utils.PIOSerializer
import dev.deadzone.module.Dependency
import dev.deadzone.module.Logger
import dev.deadzone.socket.Connection
import dev.deadzone.socket.ServerContext
import dev.deadzone.socket.utils.SocketMessage
import dev.deadzone.socket.utils.SocketMessageHandler
import io.ktor.util.date.getTimeMillis
import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import java.io.File
import java.io.FileInputStream
import java.io.InputStreamReader
import java.util.zip.GZIPInputStream
import kotlin.collections.mapOf

/**
 * Handle `save` message by:
 *
 * 1. Save
 *
 *
 * Save message is allegedly one-way signal from client to server.
 * Client send save message and expects server to save it, that's it.
 *
 */
class SaveHandler(private val context: ServerContext) : SocketMessageHandler {
    override fun match(message: SocketMessage): Boolean {
        return message.contains("s") or (message.type?.equals("s") == true)
    }

    override suspend fun handle(
        connection: Connection,
        message: SocketMessage,
        send: suspend (ByteArray) -> Unit
    ) {
        val body = message.getMap("s")
        val data = body?.get("data") as? Map<String, Any?>
        val type = data?.get("_type") as? String
        val saveId = body?.get("id") as? String

        Logger.socketPrint("Save message got: type:$type | id:$saveId")

        // Note: the game typically send and expects JSON data for save message
        // encode JSON response to string before using PIO serialization

        when (type) {
            "get_offers" -> {

            }

            "chat_getContactsBlocks" -> {

            }

            "mis_start" -> {
                val msg = listOf(
                    "r",
                    saveId ?: "m",
                    getTimeMillis(),
                    missionStartObjectResponse.copy(id = saveId ?: "")
                )
                send(PIOSerializer.serialize(msg))
            }

            "bld_move" -> {
                val x = (data["tx"] as? Number)?.toInt() ?: 0
                val y = (data["ty"] as? Number)?.toInt() ?: 0
                val r = (data["rotation"] as? Number)?.toInt() ?: 0
                val buildingId = data["id"] // use this to save in DB
                Logger.socketPrint("Building move for $saveId and $buildingId to $x,$y|r:$r")

                val response = SaveBuildingResponse(
                    success = true, x = x, y = y, r = r
                )
                val responseJson = Dependency.json.encodeToString(response)

                val msg = listOf(
                    "r",
                    saveId ?: "m",
                    getTimeMillis(),
                    responseJson
                )
                println(msg)
                send(PIOSerializer.serialize(msg))
            }

            else -> {
                Logger.unimplementedSocket("Handled 's' message but unhandled data type: $type from data=$data")
            }
        }
    }
}

@Serializable
data class SaveBuildingResponse(
    val success: Boolean = true,
    val x: Int,
    val y: Int,
    val r: Int,
    val coins: Int? = null, // used in NetworkMessage.SEND_RESPONSE, interpreted as CASH
    val skills: Map<String, SkillState>? = null, // used in NetworkMessage.SEND_RESPONSE
)

fun loadSceneXML(filePath: String): String {
    val file = File(filePath)
    GZIPInputStream(FileInputStream(file)).use { gzipStream ->
        InputStreamReader(gzipStream, Charsets.UTF_8).use { reader ->
            return reader.readText()
        }
    }
}

val sceneXMLString = loadSceneXML("static/game/data/xml/scenes/street-small-1.xml.gz")

// SaveDataMethod.MISSION_START, MissionData.as line 685
@Serializable
data class MissionStartResponse(
    val disabled: Boolean = false,
    val id: String,
    val time: Int,
    val assignmentType: String,
    val areaClass: String,
    val automated: Boolean = false,
    val sceneXML: String,
    val z: List<Zombie>,
    val allianceAttackerEnlisting: Boolean,
    val allianceAttackerLockout: Boolean,
    val allianceAttackerAllianceId: String?,
    val allianceAttackerAllianceTag: String?,
    val allianceMatch: Boolean,
    val allianceRound: Int,
    val allianceRoundActive: Boolean,
    val allianceError: Boolean,
    val allianceAttackerWinPoints: Int
)

@Serializable
data class Zombie(
    val id: String,
    val type: String,
    val level: Int
)

val missionStartObjectResponse = MissionStartResponse(
    id = "3C8B804F-A1FB-88DE-1C26-6980A1E97E92",
    time = 123456,
    assignmentType = "raid",
    areaClass = "riversideNorth",
    automated = false,
    sceneXML = sceneXMLString,
    z = listOf(
        Zombie(id = "fat-walker", type = "fat-walker", level = 10),
        Zombie(id = "fat-walker", type = "fat-walker", level = 12)
    ),
    allianceAttackerEnlisting = false,
    allianceAttackerLockout = false,
    allianceAttackerAllianceId = null,
    allianceAttackerAllianceTag = null,
    allianceMatch = false,
    allianceRound = 0,
    allianceRoundActive = false,
    allianceError = false,
    allianceAttackerWinPoints = 0
)
