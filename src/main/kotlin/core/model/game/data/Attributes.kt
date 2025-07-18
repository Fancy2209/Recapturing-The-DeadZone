package dev.deadzone.core.model.game.data

import kotlinx.serialization.Serializable

@Serializable
data class Attributes(
    val health: Double = 0.0,
    val combatProjectile: Double = 0.0,
    val combatMelee: Double = 0.0,
    val combatImprovised: Double = 0.0,
    val movement: Double = 0.0,
    val scavenge: Double = 0.0,
    val healing: Double = 0.0,
    val trapSpotting: Double = 0.0,
    val trapDisarming: Double = 0.0,
    val injuryChance: Double = 0.0
)
