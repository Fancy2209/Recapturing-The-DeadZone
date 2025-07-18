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
    val qty: UInt = 1,
    val quality: Int?,
    val bind: UInt?,
    val tradable: Boolean?,
    val disposable: Boolean?,
    val ctrType: UInt?,
    val ctrVal: Int?,
    val craft: CraftingInfo?,
    val name: String?,
    val specData: ItemBonusStats?,
    val type: String,  // added from createItemFromObject of ItemFactory
    val duplicate: boolean,  // added from deserialize of Inventory
)
