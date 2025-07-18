package dev.deadzone.core.model.game.data

import kotlinx.serialization.Serializable
import dev.deadzone.core.model.game.data.Item

@Serializable
data class Inventory(
    val inventory: List<Item>,
    val schematics: ByteArray,  // see line 643 of Inventory.as
)
