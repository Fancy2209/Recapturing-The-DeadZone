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
        fun playerM(): SurvivorAppearance {
            return SurvivorAppearance(
                skinColor = "body-1skin-light1M",
                upper = "body-upper-tshirtM",
                lower = "body-lower-pantsM",
                hair = "hair-M-08",
                facialHair = null,
                hairColor = "hair-brown",
                forceHair = false,
                hideGear = false
            )
        }

        fun fighterM(): SurvivorAppearance {
            return SurvivorAppearance(
                skinColor = "body-1skin-light1M",
                upper = "body-upper-fighterM",
                lower = "body-lower-fighterM",
                hair = "hair-M-08",
                facialHair = null,
                hairColor = "hair-brown",
                forceHair = false,
                hideGear = false
            )
        }

        fun reconF(): SurvivorAppearance {
            return SurvivorAppearance(
                skinColor = "body-1skin-light1M",
                upper = "body-upper-reconF",
                lower = "body-lower-reconF",
                hair = "hair-M-08",
                facialHair = null,
                hairColor = "hair-brown",
                forceHair = false,
                hideGear = false
            )
        }
    }
}
