package dev.deadzone.core.model.game.data

import dev.deadzone.core.data.HardcodedData
import dev.deadzone.core.model.game.data.injury.Injury
import dev.deadzone.core.model.game.data.injury.InjuryList
import kotlinx.serialization.EncodeDefault
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
    val appearance: SurvivorAppearance? = null, // HumanAppearance > SurvivorAppearance
    val scale: Double = 1.22,
    val voice: String,
    val accessories: Map<Int, String>,  // string is accessory id
    val maxClothingAccessories: Int
) {
    companion object {
        fun playerM(): Survivor {
            return Survivor(
                id = HardcodedData.PLAYER_SRV_ID,
                title = "MercifulLeader",
                firstName = "Merciful",
                lastName = "Leader",
                gender = Gender_Constants.MALE.value,
                portrait = null,
                classId = SurvivorClassConstants_Constants.PLAYER.value,
                morale = Morale().maps,
                injuries = InjuryList().list,
                level = 50,
                xp = 1000,
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

        fun reconF(): Survivor {
            return Survivor(
                id = "srv-recon-f",
                title = "NoisyRecon",
                firstName = "Noisy",
                lastName = "Recon",
                gender = Gender_Constants.FEMALE.value,
                portrait = null,
                classId = SurvivorClassConstants_Constants.RECON.value,
                morale = Morale().maps,
                injuries = InjuryList().list,
                level = 50,
                xp = 1000,
                missionId = null,
                assignmentId = null,
//                reassignTimer = null,
                appearance = SurvivorAppearance.dummy(),
                scale = 1.22,
                voice = "asian-f",
                accessories = mapOf(),
                maxClothingAccessories = 10
            )
        }

        fun fighterM(): Survivor {
            return Survivor(
                id = "srv-fighter-m",
                title = "AngryFighter",
                firstName = "Angry",
                lastName = "Fighter",
                gender = Gender_Constants.MALE.value,
                portrait = null,
                classId = SurvivorClassConstants_Constants.FIGHTER.value,
                morale = Morale().maps,
                injuries = InjuryList().list,
                level = 50,
                xp = 1000,
                missionId = null,
                assignmentId = null,
//                reassignTimer = null,
                appearance = SurvivorAppearance.dummy(),
                scale = 1.18,
                voice = "asian-m",
                accessories = mapOf(),
                maxClothingAccessories = 10
            )
        }
    }
}
