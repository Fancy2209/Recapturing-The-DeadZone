package dev.deadzone.core.compound

import dev.deadzone.core.PlayerService
import dev.deadzone.core.model.game.data.GameResources

class CompoundService(private val compoundRepository: CompoundRepository) : PlayerService {
    private lateinit var _resources: GameResources
    val resources: GameResources
        get() = _resources

    override suspend fun init(playerId: String) = runCatching {
        _resources = compoundRepository.getGameResources(playerId)!!
    }
}
