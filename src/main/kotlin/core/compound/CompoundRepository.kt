package dev.deadzone.core.compound

import dev.deadzone.core.model.game.data.BuildingLike
import dev.deadzone.core.model.game.data.GameResources

interface CompoundRepository {
    suspend fun getGameResources(playerId: String): Result<GameResources>
    suspend fun updateGameResources(
        playerId: String,
        updateAction: suspend (GameResources) -> GameResources
    ): Result<Unit>

    suspend fun getBuildings(playerId: String): Result<List<BuildingLike>>
    suspend fun updateBuilding(
        playerId: String,
        bldId: String,
        updateAction: suspend (BuildingLike) -> BuildingLike
    ): Result<Unit>
}
