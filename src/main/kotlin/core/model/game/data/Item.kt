@file:OptIn(ExperimentalSerializationApi::class)

package dev.deadzone.core.model.game.data

import dev.deadzone.core.data.HardcodedData
import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable
import java.util.*

@Serializable
data class Item(
    // Item has many fields, many of these aren't needed; however,
    // In the client-side, item factory always check whether the fields are present or not
    // If they are, they will use it without checking null (silent NPE is very often here)
    // This is why we shouldn't encode them if we don't intend to specify the field
    @EncodeDefault(EncodeDefault.Mode.NEVER) val id: String,
    @EncodeDefault(EncodeDefault.Mode.NEVER) val new: Boolean = false,
    @EncodeDefault(EncodeDefault.Mode.NEVER) val storeId: String? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER) val bought: Boolean = false,
    @EncodeDefault(EncodeDefault.Mode.NEVER) val mod1: String? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER) val mod2: String? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER) val mod3: String? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER) val type: String,
    @EncodeDefault(EncodeDefault.Mode.NEVER) val level: Int = 0,
    @EncodeDefault(EncodeDefault.Mode.NEVER) val qty: UInt = 1u,
    @EncodeDefault(EncodeDefault.Mode.NEVER) val quality: Int? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER) val bind: UInt? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER) val tradable: Boolean? = true,
    @EncodeDefault(EncodeDefault.Mode.NEVER) val disposable: Boolean? = true,
    @EncodeDefault(EncodeDefault.Mode.NEVER) val ctrType: UInt? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER) val ctrVal: Int? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER) val craft: CraftingInfo? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER) val name: String? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER) val specData: ItemBonusStats? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER) val duplicate: Boolean = false,  // added from deserialize of Inventory
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
