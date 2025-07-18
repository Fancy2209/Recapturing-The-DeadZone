package dev.deadzone.core.model.game.data

import kotlinx.serialization.Serializable

@Serializable
data class WeaponData(
    val minRange: Number = 0,
    val minEffectiveRange: Number = 0,
    val range: Number = 0,
    val minRangeMod: Number = 0,
    val maxRangeMod: Number = 0,
    val burstAvg: Int = 0,
    val roundsInMagazine: Int = 0,
    val ammoCost: Number = 0,
    val damageMax: Number = 0,
    val damageMin: Number = 0,
    val damageMult: Number = 1,
    val damageMultVsBuilding: Number = 1,
    val accuracy: Number = 0,
    val capacity: Int = 0,
    val reloadTime: Number = 0,
    val fireRate: Number = 0,
    val noise: Number = 0,
    val idleNoise: Number = 0,
    val criticalChance: Number = 0,
    val knockbackChance: Number = 0,
    val dodgeChance: Number = 0,
    val isMelee: Boolean = false,
    val isExplosive: Boolean = false,
    val attackArcCosine: Number = 0,
    val suppressionRate: Number = 0,
    val goreMultiplier: Number = 1,
    val readyTime: Number = 0
)
