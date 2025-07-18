package dev.deadzone.core.model.game.data

import kotlinx.serialization.Serializable

@Serializable
data class ItemBonusStats(
    val stat_srv: Map<String, Double>?,
    val stat_weap: Map<String, Double>?,
    val stat_gear: Map<String, Double>?
)
