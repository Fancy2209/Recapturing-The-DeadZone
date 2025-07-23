package dev.deadzone.socket.handler

import dev.deadzone.core.utils.PIOSerializer
import dev.deadzone.core.utils.parseJsonToMap
import dev.deadzone.module.Logger
import dev.deadzone.socket.Connection
import dev.deadzone.socket.ServerContext
import dev.deadzone.socket.utils.SocketMessage
import dev.deadzone.socket.utils.SocketMessageHandler
import java.io.File
import java.io.FileInputStream
import java.io.InputStreamReader
import java.util.zip.GZIPInputStream

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
        val id = body?.get("id") as? String

        Logger.socketPrint("Save message got: type:$type | id:$id")

        when (type) {
            "get_offers" -> {

            }

            "chat_getContactsBlocks" -> {

            }

            "mis_start" -> {
                val msg = listOf(
                    "ss",
                    missionStartObjectResponse.copy(id = id ?: "")
                )
                send(PIOSerializer.serialize(msg))
            }

            else -> {
                Logger.unimplementedSocket("Handled 's' message but unhandled data type: $type from data=$data")
            }
        }
    }
}

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
