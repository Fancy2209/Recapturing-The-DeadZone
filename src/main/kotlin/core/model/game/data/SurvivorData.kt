package dev.deadzone.core.model.game.data

import kotlinx.serialization.Serializable

@Serializable
data class SurvivorData(
    val id: String,
    val startXP: Int,
    val startLevel: Int,
    val endXP: Int,
    val endLevel: Int
)
