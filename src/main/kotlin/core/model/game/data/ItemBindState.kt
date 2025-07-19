package dev.deadzone.core.model.game.data

import kotlinx.serialization.Serializable

@Serializable
@JvmInline
value class ItemBindState(val value: UInt)

object ItemBindState_Constants {
    val NotBindable = ItemBindState(0u)
    val OnEquip = ItemBindState(1u)
    val Bound = ItemBindState(2u)
}
