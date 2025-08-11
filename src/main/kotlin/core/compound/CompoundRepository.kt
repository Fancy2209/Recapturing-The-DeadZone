package dev.deadzone.core.compound

import dev.deadzone.core.model.game.data.GameResources

interface CompoundRepository {
    suspend fun getGameResources(playerId: String): Result<GameResources>
}
