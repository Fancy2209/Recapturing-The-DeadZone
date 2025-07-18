package dev.deadzone.core.model.game.data.injury

import kotlinx.serialization.Serializable
import dev.deadzone.core.model.game.data.injury.Injury

@Serializable
data class InjuryList(
    val list: List<Injury>,  // casted to array in code
)
