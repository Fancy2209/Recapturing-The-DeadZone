package dev.deadzone.socket.handler.save.mission

import dev.deadzone.core.model.game.data.Zombie
import kotlinx.serialization.Serializable

@Serializable
data class GetZombieResponse(
    val z: List<Zombie> = listOf(
        Zombie.fatWalker(10),
        Zombie.fatWalker(12),
        Zombie.fatWalker(12),
        Zombie.fatWalker(16),
        Zombie.fatWalker(15),
    ),
    val max: Boolean = true, // server spawning disabled if true
)
