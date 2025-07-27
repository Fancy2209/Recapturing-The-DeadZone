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
                inventory = listOf(
                    Item.crateTutorial(),
                    Item.grenadeChristmas2(),
                    Item.p90(),
                    Item.swordUnique(),
                    Item.bladesaw(),
                    Item.freedomDesertEagle2Replica(),
                    Item.falWinter2017_3(),
                    Item.goldAK47Special(),
                    Item.helmetWastelandKnight(),
                    Item.christmasCannedMeat(),
                ),
                schematics = byteArrayOf()
            )
        }
    }
}
