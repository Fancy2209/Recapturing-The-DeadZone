package dev.deadzone.core.survivor

import dev.deadzone.core.model.game.data.Survivor
import dev.deadzone.module.Logger

/**
 * Manages player-scoped survivors.
 */
class SurvivorService(
    private val survivorRepository: SurvivorRepository
) {
    private val survivors = mutableListOf<Survivor>()

    suspend fun getSurvivorById(playerId: String, srvId: String?): Survivor? {
        val srvs = survivorRepository.getSurvivorsOfPlayerId(playerId)
        survivors.addAll(srvs)

        val result = survivors.find { it.id == srvId }
        if (result == null) {
            Logger.warn { "Couldn't find survivor of id=$srvId for player=$playerId" }
        }
        return result
    }
}
