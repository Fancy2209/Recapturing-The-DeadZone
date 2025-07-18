package dev.deadzone.core.model.game.data

import kotlinx.serialization.Serializable
import dev.deadzone.core.model.game.data.Building
import dev.deadzone.core.model.game.data.Item

@Serializable
data class JunkBuilding(
    val building: Building,
    val items: List<Item> = listOf(),
    val pos: String,  // position separated by , for x y z
    val rot: String,  // rotation separated by , for x y z
)
