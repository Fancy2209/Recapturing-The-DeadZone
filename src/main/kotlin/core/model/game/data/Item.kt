package dev.deadzone.core.model.game.data

import kotlinx.serialization.Serializable
import dev.deadzone.core.model.game.data.CraftingInfo
import dev.deadzone.core.model.game.data.ItemBonusStats

@Serializable
data class Item(
    val id: String,
    val new: Boolean = false,
    val storeId: String?,
    val bought: Boolean = false,
    val mod1: String? = null,
    val mod2: String? = null,
    val mod3: String?,
    val type: String = id,
    val level: Int = 0,
    val qty: UInt = 1u,
    val quality: Int?,
    val bind: UInt?,
    val tradable: Boolean?,
    val disposable: Boolean?,
    val ctrType: UInt?,
    val ctrVal: Int?,
    val craft: CraftingInfo?,
    val name: String?,
    val specData: ItemBonusStats?,
    val duplicate: Boolean,  // added from deserialize of Inventory
) {
    companion object {
        fun crateTutorial(): Item {
            return Item(
                id = "crate-tutorial",
                new = false,
                storeId = null,
                bought = false,
                mod1 = null,
                mod2 = null,
                mod3 = null,
                level = 0,
                qty = 1u,
                quality = 1,
                bind = 1u,
                tradable = true,
                disposable = true,
                ctrType = null,
                ctrVal = null,
                craft = null,
                name = "Crate tutorial?",
                specData = null,
                duplicate = false
            )
        }

        fun grenadeChristmas2(): Item {
            return Item(
                id = "grenade-christmas-2",
                new = false,
                storeId = null,
                bought = false,
                mod1 = null,
                mod2 = null,
                mod3 = null,
                level = 1,
                qty = 1u,
                quality = 1,
                bind = 1u,
                tradable = true,
                disposable = true,
                ctrType = null,
                ctrVal = null,
                craft = null,
                name = "what is this?",
                specData = null,
                duplicate = false
            )
        }

        fun p90(): Item {
            return Item(
                id = "p90",
                new = true,
                storeId = null,
                bought = false,
                mod1 = null,
                mod2 = null,
                mod3 = null,
                level = 37,
                qty = 1u,
                quality = 3,
                bind = 1u,
                tradable = true,
                disposable = true,
                ctrType = null,
                ctrVal = null,
                craft = null,
                name = null,
                specData = null,
                duplicate = false
            )
        }

        fun goldAK47Special(): Item {
            return Item(
                id = "goldAK47-special",
                new = true,
                storeId = null,
                bought = false,
                mod1 = null,
                mod2 = null,
                mod3 = null,
                level = 19,
                qty = 1u,
                quality = 100,
                bind = 1u,
                tradable = true,
                disposable = true,
                ctrType = null,
                ctrVal = null,
                craft = null,
                name = null,
                specData = null,
                duplicate = false
            )
        }

        fun helmetWastelandKnight(): Item {
            return Item(
                id = "helmet-wasteland-knight",
                new = true,
                storeId = null,
                bought = false,
                mod1 = null,
                mod2 = null,
                mod3 = null,
                level = 50,
                qty = 1u,
                quality = 100,
                bind = 1u,
                tradable = true,
                disposable = true,
                ctrType = null,
                ctrVal = null,
                craft = null,
                name = null,
                specData = null,
                duplicate = false
            )
        }

        fun bladesaw(): Item {
            return Item(
                id = "bladesaw",
                new = true,
                storeId = null,
                bought = false,
                mod1 = null,
                mod2 = null,
                mod3 = null,
                level = 58,
                qty = 1u,
                quality = 50,
                bind = 1u,
                tradable = true,
                disposable = true,
                ctrType = null,
                ctrVal = null,
                craft = null,
                name = null,
                specData = null,
                duplicate = false
            )
        }

        fun falWinter2017_3(): Item {
            return Item(
                id = "fal-winter-2017-3",
                new = true,
                storeId = null,
                bought = false,
                mod1 = null,
                mod2 = null,
                mod3 = null,
                level = 59,
                qty = 1u,
                quality = 100,
                bind = 1u,
                tradable = true,
                disposable = true,
                ctrType = null,
                ctrVal = null,
                craft = null,
                name = null,
                specData = null,
                duplicate = false
            )
        }

        fun freedomDesertEagle2Replica(): Item {
            return Item(
                id = "freedom-desert-eagle-2-replica",
                new = true,
                storeId = null,
                bought = false,
                mod1 = null,
                mod2 = null,
                mod3 = null,
                level = 49,
                qty = 1u,
                quality = 100,
                bind = 1u,
                tradable = true,
                disposable = true,
                ctrType = null,
                ctrVal = null,
                craft = null,
                name = null,
                specData = null,
                duplicate = false
            )
        }

        fun swordUnique(): Item {
            return Item(
                id = "sword-unique",
                new = true,
                storeId = null,
                bought = false,
                mod1 = null,
                mod2 = null,
                mod3 = null,
                level = 49,
                qty = 1u,
                quality = 51,
                bind = 1u,
                tradable = true,
                disposable = true,
                ctrType = null,
                ctrVal = null,
                craft = null,
                name = null,
                specData = null,
                duplicate = false
            )
        }

        fun christmasCannedMeat(): Item {
            return Item(
                id = "christmas-canned-meat",
                new = true,
                storeId = null,
                bought = false,
                mod1 = null,
                mod2 = null,
                mod3 = null,
                level = 1,
                qty = 1u,
                quality = null,
                bind = 1u,
                tradable = true,
                disposable = true,
                ctrType = null,
                ctrVal = null,
                craft = null,
                name = null,
                specData = null,
                duplicate = false
            )
        }
    }
}
