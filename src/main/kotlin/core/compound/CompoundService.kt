package dev.deadzone.core.compound

import dev.deadzone.core.PlayerService
import dev.deadzone.core.model.game.data.Building
import dev.deadzone.core.model.game.data.BuildingLike
import dev.deadzone.core.model.game.data.GameResources
import dev.deadzone.core.model.game.data.JunkBuilding
import dev.deadzone.core.model.game.data.Survivor
import dev.deadzone.utils.LogConfigSocketError
import dev.deadzone.utils.Logger

class CompoundService(private val compoundRepository: CompoundRepository) : PlayerService {
    private lateinit var resources: GameResources
    private val buildings = mutableListOf<BuildingLike>()
    private lateinit var playerId: String

    fun getResources() = resources
    fun getBuildings() = buildings

    suspend fun updateResources(newResources: GameResources) {
        val result = compoundRepository.updateGameResources(playerId) { newResources }
        result.onFailure {
            Logger.error(LogConfigSocketError) { "Error on updateResources: ${it.message}" }
        }
        result.onSuccess {
            this.resources = newResources
        }
    }

    suspend fun updateBuilding(bldId: String, newBuilding: BuildingLike) {
        val result = compoundRepository.updateBuilding(playerId, bldId) { newBuilding }
        result.onFailure {
            Logger.error(LogConfigSocketError) { "Error on updateBuilding: ${it.message}" }
        }
        result.onSuccess {
            this.buildings.forEachIndexed { idx, bldLike ->
                when (bldLike) {
                    is Building -> {
                        if (bldLike.id == bldId) {
                            buildings[idx] = newBuilding
                        }
                    }
                    is JunkBuilding -> {
                        if (bldLike.id == bldId) {
                            buildings[idx] = newBuilding
                        }
                    }
                }
            }
        }
    }

    override suspend fun init(playerId: String): Result<Unit> {
        return runCatching {
            this.playerId = playerId
            val _resources = compoundRepository.getGameResources(playerId).getOrThrow()
            val _buildings = compoundRepository.getBuildings(playerId).getOrThrow()
            resources = _resources
            buildings.addAll(_buildings)
        }
    }
}
