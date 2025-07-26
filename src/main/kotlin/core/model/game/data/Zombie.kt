package dev.deadzone.core.model.game.data

import kotlinx.serialization.Serializable

@Serializable
data class Zombie(
    val id: String,
    val type: String,
    val level: Int
) {
    companion object {
        fun fatWalker(level: Int): Zombie {
            return Zombie(id = "fat-walker", type = "fat-walker", level = level)
        }
    }
}
