package dev.deadzone.core.model.game.data

import kotlinx.serialization.Serializable
import dev.deadzone.core.model.game.data.Item
import dev.deadzone.core.model.game.data.SurvivorLoadout

@Serializable
data class SurvivorLoadoutData(
    val type: String,
    val item: Item,
    val quantity: Int,
    val loadout: SurvivorLoadout
)
