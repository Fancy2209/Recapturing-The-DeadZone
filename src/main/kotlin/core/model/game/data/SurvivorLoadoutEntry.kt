package dev.deadzone.core.model.game.data

import dev.deadzone.core.data.HardcodedData
import kotlinx.serialization.Serializable

@Serializable
data class SurvivorLoadoutEntry(
    val weapon: String,  // weapon id
    val gear1: String,  // gear id
    val gear2: String,  // gear id
) {
    companion object {
        fun playerLoudout(): SurvivorLoadoutEntry {
            return SurvivorLoadoutEntry(
                weapon = HardcodedData.PLAYER_WEP_ID,
                gear1 = "",
                gear2 = "",
            )
        }

        fun fighterLoadout(): SurvivorLoadoutEntry {
            return SurvivorLoadoutEntry(
                weapon = HardcodedData.FIGHTER_WEP_ID,
                gear1 = "",
                gear2 = "",
            )
        }

        fun reconLoadout(): SurvivorLoadoutEntry {
            return SurvivorLoadoutEntry(
                weapon = HardcodedData.RECON_WEP_ID,
                gear1 = "",
                gear2 = "",
            )
        }
    }
}
