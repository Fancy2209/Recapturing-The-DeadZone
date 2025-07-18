package dev.deadzone.core.model.game.data

import kotlinx.serialization.Serializable

@Serializable
data class GameResources(
    val cash: Int = 0,
    val wood: Int = 0,
    val metal: Int = 0,
    val cloth: Int = 0,
    val water: Int = 0,
    val food: Int = 0,
    val ammunition: Int = 0
)
