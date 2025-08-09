package dev.deadzone.core.survivor

import dev.deadzone.core.PlayerService
import dev.deadzone.core.model.game.data.Survivor
import dev.deadzone.module.Logger

/**
 * Manages survivors.
 */
class SurvivorService(
    private val survivorRepository: SurvivorRepository
): PlayerService {
    private val survivors = mutableListOf<Survivor>()
    private lateinit var playerId: String // for simple debug

    fun getSurvivorById(srvId: String?): Survivor? {
        val result = survivors.find { it.id == srvId }
        if (result == null) {
            Logger.warn { "Couldn't find survivor of id=$srvId for player=$playerId" }
        }
        return result
    }

    override suspend fun init(playerId: String) {
        this.playerId = playerId
        val srvs = survivorRepository.getSurvivorsOfPlayerId(playerId)
        survivors.addAll(srvs)
    }
}
