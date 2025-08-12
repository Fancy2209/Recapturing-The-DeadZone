package dev.deadzone.core.compound

import com.mongodb.client.model.Filters
import com.mongodb.client.model.FindOneAndDeleteOptions
import com.mongodb.client.model.Updates
import com.mongodb.kotlin.client.coroutine.MongoCollection
import dev.deadzone.core.data.runMongoCatching
import dev.deadzone.core.model.game.data.Building
import dev.deadzone.core.model.game.data.BuildingLike
import dev.deadzone.core.model.game.data.GameResources
import dev.deadzone.core.model.game.data.JunkBuilding
import dev.deadzone.data.collection.PlayerObjects
import kotlinx.coroutines.flow.firstOrNull
import org.bson.Document

class CompoundRepositoryMongo(val objCollection: MongoCollection<PlayerObjects>) : CompoundRepository {
    override suspend fun getGameResources(playerId: String): Result<GameResources> {
        return runMongoCatching("No player found with id=$playerId") {
            val filter = Filters.eq("playerId", playerId)
            objCollection
                .find(filter)
                .firstOrNull()
                ?.resources
        }
    }

    override suspend fun updateGameResources(
        playerId: String,
        updateAction: suspend (GameResources) -> GameResources
    ): Result<Unit> {
        return runMongoCatching {
            val filter = Filters.eq("playerId", playerId)
            val playerObj = objCollection
                .find(filter)
                .firstOrNull()
                ?: throw NoSuchElementException("No player found with id=$playerId")

            val updatedResources = updateAction(playerObj.resources)
            val updateSet = Updates.set("resources", updatedResources)

            objCollection.updateOne(filter, updateSet)
            Unit
        }
    }

    override suspend fun createBuilding(
        playerId: String,
        createAction: suspend () -> BuildingLike
    ): Result<Unit> {
        return runMongoCatching {
            val filter = Filters.eq("playerId", playerId)
            val updateAdd = Updates.addToSet("buildings", createAction())

            objCollection.updateOne(filter, updateAdd)
            Unit
        }
    }

    override suspend fun getBuildings(playerId: String): Result<List<BuildingLike>> {
        return runMongoCatching("No player found with id=$playerId") {
            val filter = Filters.eq("playerId", playerId)
            objCollection
                .find(filter)
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
            val filter = Filters.eq("playerId", playerId)

            val playerObj = objCollection
                .find(filter)
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

            val updateSet = Updates.set("buildings.$index", updatedBuilding)

            objCollection.updateOne(filter, updateSet)
            Unit
        }
    }

    override suspend fun deleteBuilding(playerId: String, bldId: String): Result<Unit> {
        return runMongoCatching {
            val filter = Filters.eq("playerId", playerId)
            val updateDelete = Updates.pull("buildings", Document("id", bldId))

            val updateResult = objCollection.updateOne(filter, updateDelete)

            if (updateResult.matchedCount != 1L) {
                throw NoSuchElementException("No player found with id=$playerId")
            }

            if (updateResult.modifiedCount != 1L) {
                throw NoSuchElementException("No building found for bldId=$bldId on playerId=$playerId")
            }
            Unit
        }
    }
}
