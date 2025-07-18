package dev.deadzone.core.model.game.data

import kotlinx.serialization.Serializable
import dev.deadzone.core.model.game.data.Gender
import dev.deadzone.core.model.game.data.HumanAppearance
import dev.deadzone.core.model.game.data.injury.InjuryList
import dev.deadzone.core.model.game.data.Morale
import dev.deadzone.core.model.game.data.SurvivorClass
import dev.deadzone.core.model.game.data.TimerData

@Serializable
data class Survivor(
    val id: String,
    val title: String,
    val firstName: String = "",
    val lastName: String = "",
    val gender: Gender,
    val portrait: String? = null,
    val classId: SurvivorClass,
    val morale: Morale?,
    val injuries: InjuryList?,
    val level: Int,
    val xp: Int,
    val missionId: String?,
    val assignmentId: String?,
    val reassignTimer: TimerData?,
    val appearance: HumanAppearance?,
    val scale: Double = 1.22,
    val voice: String,
    val accessories: Map<Int, String>,  // string is accessory id
    val maxClothingAccessories: Int
)
