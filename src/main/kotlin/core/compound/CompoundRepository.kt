package dev.deadzone.core.compound

import dev.deadzone.core.model.game.data.BuildingLike
import dev.deadzone.core.model.game.data.GameResources

interface CompoundRepository {
    // Resource C_U_
    suspend fun getGameResources(playerId: String): Result<GameResources>
    suspend fun updateGameResources(
        playerId: String,
        updateAction: suspend (GameResources) -> GameResources
    ): Result<Unit>

    // Building CRUD
    suspend fun createBuilding(
        playerId: String,
        createAction: suspend () -> BuildingLike
    ): Result<Unit>
    suspend fun getBuildings(playerId: String): Result<List<BuildingLike>>
    suspend fun updateBuilding(
        playerId: String,
        bldId: String,
        updateAction: suspend (BuildingLike) -> BuildingLike
    ): Result<Unit>
    suspend fun deleteBuilding(playerId: String, bldId: String): Result<Unit>
}
