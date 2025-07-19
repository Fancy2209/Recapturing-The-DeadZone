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
) {
    companion object {
        fun dummy(id: String, classId: SurvivorClass, gender: Gender): Survivor {
            return Survivor(
                id = id,
                title = "The survivor",
                firstName = "The",
                lastName = "survivor",
                gender = Gender_Constants.MALE,
                portrait = null,
                classId = classId,
                morale = null,
                injuries = null,
                level = 1,
                xp = 0,
                missionId = null,
                assignmentId = null,
                reassignTimer = null,
                appearance = null,
                scale = 1.22,
                voice = "asian-m",
                accessories = mapOf(),
                maxClothingAccessories = TODO()
            )
        }
    }
}
