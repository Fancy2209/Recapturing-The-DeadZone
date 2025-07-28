@file:OptIn(ExperimentalSerializationApi::class)

package dev.deadzone.core.model.game.data

import dev.deadzone.core.data.HardcodedData
import kotlinx.serialization.Serializable
import dev.deadzone.core.model.game.data.CraftingInfo
import dev.deadzone.core.model.game.data.ItemBonusStats
import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.ExperimentalSerializationApi
import java.util.UUID
import kotlin.random.Random
import kotlin.uuid.Uuid

@Serializable
data class Item constructor(
    val id: String,
    val new: Boolean = false,
    val storeId: String? = null,
    val bought: Boolean = false,
    val mod1: String? = null,
    val mod2: String? = null,
    val mod3: String? = null,
    val type: String,
    val level: Int = 0,
    val qty: UInt = 1u,
    val quality: Int? = null,
    val bind: UInt? = null,
    val tradable: Boolean? = true,
    val disposable: Boolean? = true,
    val ctrType: UInt? = null,
    val ctrVal: Int? = null,
    val craft: CraftingInfo? = null,
    val name: String? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER) val specData: ItemBonusStats? = null,
    val duplicate: Boolean = false,  // added from deserialize of Inventory
) {
    companion object {
        fun crateTutorial(): Item {
            return Item(id = UUID.randomUUID().toString(), type = "crate-tutorial")
        }

        fun keyHercLevel1(): Item {
            return Item(id = UUID.randomUUID().toString(), type = "key-herc-level-1", new = false, qty = 10u)
        }

        fun grenadeChristmas2(): Item {
            return Item(id = UUID.randomUUID().toString(), type = "grenade-christmas-2")
        }

        fun p90(): Item {
            return Item(id = UUID.randomUUID().toString(), type = "p90", level = 37, quality = 3)
        }

        fun goldAK47Special(): Item {
            return Item(
                id = UUID.randomUUID().toString(),
                type = "goldAK47-special",
                level = 19,
                quality = 100,
                bind = 1u
            )
        }

        fun helmetWastelandKnight(): Item {
            return Item(id = UUID.randomUUID().toString(), type = "helmet-wasteland-knight", level = 50, quality = 100)
        }

        fun swordUnique(): Item {
            return Item(id = UUID.randomUUID().toString(), type = "sword-unique", level = 49, quality = 51)
        }

        fun christmasCannedMeat(): Item {
            return Item(id = UUID.randomUUID().toString(), type = "christmas-canned-meat")
        }

        fun bladesaw(): Item {
            return Item(id = HardcodedData.FIGHTER_WEP_ID, type = "bladesaw", level = 58, quality = 50)
        }

        fun freedomDesertEagle2Replica(): Item {
            return Item(
                id = HardcodedData.PLAYER_WEP_ID,
                type = "freedom-desert-eagle-2-replica",
                level = 49,
                quality = 100
            )
        }

        fun falWinter2017_3(): Item {
            return Item(id = HardcodedData.RECON_WEP_ID, type = "fal-winter-2017-3", level = 59, quality = 100)
        }
    }
}
