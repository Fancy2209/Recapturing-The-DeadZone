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
)
