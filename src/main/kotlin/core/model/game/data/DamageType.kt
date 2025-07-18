package dev.deadzone.core.model.game.data

import kotlinx.serialization.Serializable

@Serializable
data class DamageType(
    val UNKNOWN: UInt = 0,
    val MELEE: UInt = 1,
    val PROJECTILE: UInt = 2,
    val EXPLOSIVE: UInt = 3
)
