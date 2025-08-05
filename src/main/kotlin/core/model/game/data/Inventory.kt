package dev.deadzone.core.model.game.data

import dev.deadzone.core.data.DummyData
import dev.deadzone.core.model.factory.ItemFactory
import kotlinx.serialization.Serializable

@Serializable
data class Inventory(
    val inventory: List<Item> = emptyList(),
    val schematics: ByteArray = byteArrayOf(),  // see line 643 of Inventory.as
) {
    companion object {
        fun dummy(): Inventory {
            val items = listOf(
                ItemFactory.createItemFromId(idInXML = "crate-tutorial"),
                ItemFactory.createItemFromId(idInXML = "crate-tutorial"),
                ItemFactory.createItemFromId(idInXML = "crate-tutorial"),
                ItemFactory.createItemFromId(idInXML = "crate-tutorial"),
                ItemFactory.createItemFromId(idInXML = "crate-tutorial"),
                ItemFactory.createItemFromId(idInXML = "crate-tutorial"),
                ItemFactory.createItemFromId(idInXML = "crate-tutorial"),
                ItemFactory.createItemFromId(idInXML = "crate-tutorial"),
                ItemFactory.createItemFromId(idInXML = "crate-tutorial"),
                ItemFactory.createItemFromId(idInXML = "crate-tutorial"),
                ItemFactory.createItemFromId(idInXML = "key-herc-level-1").copy(new = false, qty = 10u),
                ItemFactory.createItemFromId(idInXML = "grenade-christmas-2"),
                ItemFactory.createItemFromId(idInXML = "p90").copy(level = 37, quality = 3),
                ItemFactory.createItemFromId(idInXML = "sword-unique").copy(level = 49, quality = 51),
                ItemFactory.createItemFromId(itemId = DummyData.FIGHTER_WEP_ID, "bladesaw")
                    .copy(level = 58, quality = 50),
                ItemFactory.createItemFromId(itemId = DummyData.PLAYER_WEP_ID, "freedom-desert-eagle-2-replica")
                    .copy(level = 49, quality = 100),
                ItemFactory.createItemFromId(itemId = DummyData.RECON_WEP_ID, "fal-winter-2017-3")
                    .copy(level = 59, quality = 100),
                ItemFactory.createItemFromId(idInXML = "goldAK47-special").copy(level = 19, quality = 100, bind = 1u),
                ItemFactory.createItemFromId(idInXML = "helmet-wasteland-knight").copy(level = 50, quality = 100),
                ItemFactory.createItemFromId(idInXML = "christmas-canned-meat")
            )

            return Inventory(
                inventory = items,
                schematics = byteArrayOf()
            )
        }

        fun newgame(): Inventory {
            val items = listOf(
                Item(type = "pocketKnife")
            )
            return Inventory(
                inventory = items,
                schematics = byteArrayOf()
            )
        }
    }
}
