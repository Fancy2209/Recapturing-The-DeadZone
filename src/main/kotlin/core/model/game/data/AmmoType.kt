package dev.deadzone.core.model.game.data

import kotlinx.serialization.Serializable

@Serializable
data class AmmoType(
    val NONE: UInt = 0,
    val ARROW: UInt = 1,
    val ASSAULT_RIFLE: UInt = 2,
    val BOLT: UInt = 4,
    val LONG_RIFLE: UInt = 8,
    val PISTOL: UInt = 16,
    val SHOTGUN: UInt = 32,
    val SMG: UInt = 64
)
