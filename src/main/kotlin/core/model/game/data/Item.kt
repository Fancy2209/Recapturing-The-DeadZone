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
    val mod1: String?,
    val mod2: String?,
    val mod3: String?,
    val type: String,
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
                type = "crate",
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
                duplicate = TODO()
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
                type = "gear",
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
                duplicate = TODO()
            )
        }
    }
}
