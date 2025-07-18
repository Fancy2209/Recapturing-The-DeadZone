package dev.deadzone.core.model.game.data

import kotlinx.serialization.Serializable
import dev.deadzone.core.model.game.data.Building

@Serializable
data class BuildingCollection(
    val list: List<Building>
)
