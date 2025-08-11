package dev.deadzone.core.compound

import dev.deadzone.core.PlayerService
import dev.deadzone.core.model.game.data.GameResources

class CompoundService(private val compoundRepository: CompoundRepository) : PlayerService {
    private lateinit var resources: GameResources
    private lateinit var playerId: String

    fun getResources() = resources

    override suspend fun init(playerId: String): Result<Unit> {
        return runCatching {
            this.playerId = playerId
            val _resources = compoundRepository.getGameResources(playerId).getOrThrow()
            resources = _resources
        }
    }
}
