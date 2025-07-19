package dev.deadzone.core.model.game.data

import kotlinx.serialization.Serializable

@Serializable
@JvmInline
value class ItemQualityType(val value: Int)

object ItemQualityType_Constants {
    val NONE = ItemQualityType(-2147483648)
    val GREY = ItemQualityType(-1)
    val WHITE = ItemQualityType(0)
    val GREEN = ItemQualityType(1)
    val BLUE = ItemQualityType(2)
    val PURPLE = ItemQualityType(3)
    val RARE = ItemQualityType(50)
    val UNIQUE = ItemQualityType(51)
    val INFAMOUS = ItemQualityType(52)
    val PREMIUM = ItemQualityType(100)
}
