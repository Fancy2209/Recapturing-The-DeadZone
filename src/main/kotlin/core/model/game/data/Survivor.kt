package dev.deadzone.core.model.game.data

import dev.deadzone.core.model.game.data.injury.Injury
import dev.deadzone.core.model.game.data.injury.InjuryList
import kotlinx.serialization.Serializable

@Serializable
data class Survivor(
    val id: String,
    val title: String,
    val firstName: String = "",
    val lastName: String = "",
    val gender: String,
    val portrait: String? = null,
    val classId: String,
    val morale: Map<String, Double>,
    val injuries: List<Injury>,
    val level: Int,
    val xp: Int,
    val missionId: String?,
    val assignmentId: String?,
    val reassignTimer: TimerData? = null,
    val appearance: SurvivorAppearance?, // HumanAppearance > SurvivorAppearance
    val scale: Double = 1.22,
    val voice: String,
    val accessories: Map<Int, String>,  // string is accessory id
    val maxClothingAccessories: Int
) {
    companion object {
        fun dummy(id: String, classId: SurvivorClassConstants, gender: Gender): Survivor {
            return Survivor(
                id = id,
                title = "Please bro",
                firstName = "PLSBRO",
                lastName = "survivor",
                gender = gender.value,
                portrait = "https://picsum.photos/40",
                classId = classId.value,
                morale = Morale().maps,
                injuries = InjuryList().list,
                level = 5,
                xp = 100,
                missionId = null,
                assignmentId = null,
//                reassignTimer = null,
                appearance = SurvivorAppearance.dummy(),
                scale = 1.22,
                voice = "asian-m",
                accessories = mapOf(),
                maxClothingAccessories = 10
            )
        }
    }
}
