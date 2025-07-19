package dev.deadzone.core.model.game.data

import kotlinx.serialization.Serializable

@Serializable
@JvmInline
value class ItemCounterType(val value: UInt)

object ItemCounterType_Constants {
    val None = ItemCounterType(0u)
    val ZombieKills = ItemCounterType(1u)
    val HumanKills = ItemCounterType(2u)
    val SurvivorKills = ItemCounterType(3u)
}
