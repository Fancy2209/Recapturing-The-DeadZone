package dev.deadzone.core.survivor

import dev.deadzone.core.PlayerService
import dev.deadzone.core.model.game.data.HumanAppearance
import dev.deadzone.core.model.game.data.Survivor
import dev.deadzone.utils.LogConfigSocketError
import dev.deadzone.utils.Logger

/**
 * Manages survivors.
 */
class SurvivorService(
    val survivorLeaderId: String,
    private val survivorRepository: SurvivorRepository
) : PlayerService {
    private val survivors = mutableListOf<Survivor>()
    private lateinit var playerId: String // for simple debug

    fun getSurvivorLeader(): Survivor {
        return survivors.find { it.id == survivorLeaderId }
            ?: throw IllegalStateException("Survivor leader is missing for playerId=$playerId")
    }

    fun getAllSurvivors(): List<Survivor> {
        return survivors
    }

    fun getSurvivorById(srvId: String?): Survivor? {
        val result = survivors.find { it.id == srvId }
        if (result == null) {
            Logger.warn { "Couldn't find survivor of id=$srvId for player=$playerId" }
        }
        return result
    }

    suspend fun saveSurvivorAppearance(srvId: String, newAppearance: HumanAppearance) {
        var newSurvivor: Survivor? = null
        val result = survivorRepository.updateSurvivor(playerId, srvId) { srv ->
            newSurvivor = srv
            srv.copy(appearance = newAppearance)
        }
        result.onFailure {
            Logger.error(LogConfigSocketError) { "Error on saveSurvivorAppearance: ${it.message}" }
        }

        // update the in-memory data
        newSurvivor?.let {
            survivors.removeIf { it.id == srvId }
            survivors.add(newSurvivor)
        }
    }

    suspend fun updateSurvivor(srvId: String, newSurvivor: Survivor) {
        val result = survivorRepository.updateSurvivor(playerId, srvId) { newSurvivor }
        result.onFailure {
            Logger.error(LogConfigSocketError) { "Error on updateSurvivor: ${it.message}" }
        }

        // update the in-memory data
        survivors.removeIf { it.id == srvId }
        survivors.add(newSurvivor)
    }

    override suspend fun init(playerId: String): Result<Unit> {
        return runCatching {
            this.playerId = playerId
            val srvs = survivorRepository.getSurvivorsOfPlayerId(playerId)
            survivors.addAll(srvs)
        }
    }
}
