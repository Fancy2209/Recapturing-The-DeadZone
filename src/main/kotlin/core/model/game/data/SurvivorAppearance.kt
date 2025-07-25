package dev.deadzone.core.model.game.data

import kotlinx.serialization.Serializable

@Serializable
data class SurvivorAppearance(
    val skinColor: String?,
    val upper: String?,
    val lower: String?,
    val hair: String?,
    val facialHair: String?,
    val hairColor: String?,
    val forceHair: Boolean = false,
    val hideGear: Boolean = false
) {
    companion object {
        fun dummy(): SurvivorAppearance {
            // from data/models/characters/survivorClasses
            return SurvivorAppearance(
                skinColor = "body-1skin-light1M",
                upper = "body-2upper-reconM",
                lower = "body-3lower-reconM",
                hair = "hair-M-08",
                facialHair = null,
                hairColor = "hair-brown",
                forceHair = false,
                hideGear = false
            )
        }
    }
}
