package dev.deadzone.core.survivor

import dev.deadzone.core.PlayerService
import dev.deadzone.core.model.game.data.HumanAppearance
import dev.deadzone.core.model.game.data.Survivor
import dev.deadzone.utils.LogConfigSocketError
import dev.deadzone.utils.LogConfigSocketToClient
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
            ?: throw NoSuchElementException("Survivor leader is missing for playerId=$playerId")
    }

    fun getAllSurvivors(): List<Survivor> {
        return survivors
    }

    fun getSurvivorById(srvId: String?): Survivor {
        return survivors.find { it.id == srvId }
            ?: throw NoSuchElementException("Couldn't find survivor of id=$srvId for player=$playerId")
    }

    suspend fun saveSurvivorAppearance(srvId: String, newAppearance: HumanAppearance) {
        var newSurvivor: Survivor? = null
        val result = survivorRepository.updateSurvivor(playerId, srvId) { srv ->
            newSurvivor = srv.copy(appearance = newAppearance)
            newSurvivor
        }
        result.onFailure {
            Logger.error(LogConfigSocketError) { "Error on saveSurvivorAppearance: ${it.message}" }
        }
        result.onSuccess {
            newSurvivor?.let {
                this.survivors.forEachIndexed { idx, srv ->
                    if (srv.id == srvId) {
                        survivors[idx] = newSurvivor
                    }
                }
            }
        }
    }

    suspend fun updateSurvivor(srvId: String, newSurvivor: Survivor) {
        val result = survivorRepository.updateSurvivor(playerId, srvId) { newSurvivor }
        result.onFailure {
            Logger.error(LogConfigSocketToClient) { "Error on updateSurvivor: ${it.message}" }
        }
        result.onSuccess {
            if (survivors.removeIf { it.id == srvId }) {
                survivors.add(newSurvivor)
            }
        }
    }

    override suspend fun init(playerId: String): Result<Unit> {
        return runCatching {
            this.playerId = playerId
            val _survivors = survivorRepository.getSurvivors(playerId).getOrThrow()

            if (_survivors.isEmpty()) {
                Logger.warn(LogConfigSocketToClient) { "Survivor for playerId=$playerId is empty" }
            }
            survivors.addAll(_survivors)
        }
    }
}
