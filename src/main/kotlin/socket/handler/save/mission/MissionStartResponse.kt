package dev.deadzone.socket.handler.save.mission

import dev.deadzone.core.model.game.data.Zombie
import dev.deadzone.socket.handler.save.BaseResponse
import kotlinx.serialization.Serializable
import java.io.InputStreamReader
import java.util.zip.GZIPInputStream

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
    val allianceAttackerWinPoints: Int,
): BaseResponse()

fun decideSceneXML(): String {
    return ""
}

fun loadSceneXML(filename: String): String {
    val path = "static/game/data/xml/scenes/" + filename
    val resourceStream = object {}.javaClass.classLoader.getResourceAsStream(path)
        ?: throw IllegalArgumentException("Resource not found: $path")

    GZIPInputStream(resourceStream).use { gzipStream ->
        InputStreamReader(gzipStream, Charsets.UTF_8).use { reader ->
            return reader.readText()
        }
    }
}
