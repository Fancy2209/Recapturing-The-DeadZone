package dev.deadzone.core.model.game.data

import dev.deadzone.core.model.game.data.injury.Injury
import kotlinx.serialization.Serializable
import dev.deadzone.core.model.game.data.injury.InjuryList

@Serializable
data class Survivor(
    val id: String,
    val title: String,
    val firstName: String = "",
    val lastName: String = "",
    val gender: Gender,
    val portrait: String? = null,
    val classId: String,
    val morale: Map<String, Double>,
    val injuries: List<Injury>,
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
) {
    companion object {
        fun dummy(id: String, classId: SurvivorClassConstants, gender: Gender): Survivor {
            return Survivor(
                id = id,
                title = "The survivor",
                firstName = "The",
                lastName = "survivor",
                gender = gender,
                portrait = null,
                classId = classId.value,
                morale = Morale().maps,
                injuries = InjuryList().list,
                level = 1,
                xp = 0,
                missionId = null,
                assignmentId = null,
                reassignTimer = null,
                appearance = null,
                scale = 1.22,
                voice = "asian-m",
                accessories = mapOf(),
                maxClothingAccessories = 10
            )
        }
    }
}
