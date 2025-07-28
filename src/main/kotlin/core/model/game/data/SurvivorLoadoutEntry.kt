package dev.deadzone.core.model.game.data

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
                weapon = "p90",
                gear1 = "",
                gear2 = "",
            )
        }

        fun fighterLoadout(): SurvivorLoadoutEntry {
            return SurvivorLoadoutEntry(
                weapon = "bladesaw",
                gear1 = "",
                gear2 = "",
            )
        }

        fun reconLoadout(): SurvivorLoadoutEntry {
            return SurvivorLoadoutEntry(
                weapon = "fal-winter-2017-3",
                gear1 = "",
                gear2 = "",
            )
        }
    }
}
