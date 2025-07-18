package dev.deadzone.core.model.game.data.injury

import kotlinx.serialization.Serializable
import dev.deadzone.core.model.game.data.TimerData

@Serializable
data class Injury(
    val id: String,
    val type: String,
    val location: String,
    val severity: String,
    val damage: Number,
    val morale: Number,
    val timer: TimerData?
)
