package dev.deadzone.core.model.game.data

import kotlinx.serialization.Serializable

@Serializable
data class TimerData(
    val start: Double,
    val length: Double,
    val data: Map<String, Double>?
)
