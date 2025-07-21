package dev.deadzone.core.model.game.data

import kotlinx.serialization.Serializable
import dev.deadzone.core.model.game.data.Item

@Serializable
data class Inventory(
    val inventory: List<Item> = listOf(),
    val schematics: ByteArray = byteArrayOf(),  // see line 643 of Inventory.as
) {
    companion object {
        fun dummy(): Inventory {
            return Inventory(
                inventory = listOf(Item.crateTutorial()),
                schematics = byteArrayOf()
            )
        }
    }
}
