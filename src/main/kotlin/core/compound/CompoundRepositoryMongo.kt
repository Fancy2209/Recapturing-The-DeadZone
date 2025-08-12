package dev.deadzone.core.compound

import com.mongodb.client.model.Filters
import com.mongodb.client.model.Updates
import com.mongodb.kotlin.client.coroutine.MongoCollection
import dev.deadzone.core.data.runMongoCatching
import dev.deadzone.core.model.game.data.Building
import dev.deadzone.core.model.game.data.BuildingLike
import dev.deadzone.core.model.game.data.GameResources
import dev.deadzone.core.model.game.data.JunkBuilding
import dev.deadzone.data.collection.PlayerObjects
import kotlinx.coroutines.flow.firstOrNull

class CompoundRepositoryMongo(val objCollection: MongoCollection<PlayerObjects>) : CompoundRepository {
    override suspend fun getGameResources(playerId: String): Result<GameResources> {
        return runMongoCatching("No player found with id=$playerId") {
            objCollection
                .find(Filters.eq("playerId", playerId))
                .firstOrNull()
                ?.resources
        }
    }

    override suspend fun updateGameResources(
        playerId: String,
        updateAction: suspend (GameResources) -> GameResources
    ): Result<Unit> {
        return runMongoCatching {
            val playerObj = objCollection
                .find(Filters.eq("playerId", playerId))
                .firstOrNull()
                ?: throw NoSuchElementException("No player found with id=$playerId")

            val updated = updateAction(playerObj.resources)
            val update = Updates.set("resources", updated)

            objCollection.updateOne(Filters.eq("playerId", playerId), update)
            Unit
        }
    }

    override suspend fun getBuildings(playerId: String): Result<List<BuildingLike>> {
        return runMongoCatching("No player found with id=$playerId") {
            objCollection
                .find(Filters.eq("playerId", playerId))
                .firstOrNull()
                ?.buildings
        }
    }

    override suspend fun updateBuilding(
        playerId: String,
        bldId: String,
        updateAction: suspend (BuildingLike) -> BuildingLike
    ): Result<Unit> {
        return runMongoCatching {
            val playerObj = objCollection
                .find(Filters.eq("playerId", playerId))
                .firstOrNull()
                ?: throw NoSuchElementException("No player found with id=$playerId")

            val index = playerObj.buildings.indexOfFirst {
                when (it) {
                    is Building -> {
                        it.id == bldId
                    }

                    is JunkBuilding -> {
                        it.id == bldId
                    }
                }
            }
            if (index == -1) throw NoSuchElementException("Building for playerId=$playerId bldId=$bldId not found")

            val currentBuilding = playerObj.buildings[index]
            val updatedBuilding = updateAction(currentBuilding)

            val update = Updates.set("buildings.$index", updatedBuilding)

            objCollection.updateOne(Filters.eq("playerId", playerId), update)
            Unit
        }
    }
}
