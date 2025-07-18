package dev.deadzone.core.model.game.data

import kotlinx.serialization.Serializable
import dev.deadzone.core.model.game.data.Item
import dev.deadzone.core.model.game.data.MissionStats
import dev.deadzone.core.model.game.data.SurvivorData
import dev.deadzone.core.model.game.data.TimerData

@Serializable
data class MissionData(
    val id: String,
    val player: SurvivorData,
    val stats: MissionStats?,
    val xpEarned: Int,
    val xp: Map<String, Int> = mapOf(),
    val completed: Boolean,
    val assignmentId: String,
    val assignmentType: String,
    val playerId: String?,
    val compound: Boolean,
    val areaLevel: Int,
    val areaId: String,
    val type: String,
    val suburb: String,
    val automated: Boolean,
    val survivors: List<String> = listOf(),  // survivor ids
    val srvDown: List<String> = listOf(),  // survivor ids
    val buildingsDestroyed: List<String> = listOf(),  // building ids
    val returnTimer: TimerData?,
    val lockTimer: TimerData?,
    val loot: List<Item> = listOf(),
    val highActivityIndex: Int?
)
