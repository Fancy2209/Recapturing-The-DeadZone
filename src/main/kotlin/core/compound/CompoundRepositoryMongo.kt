package dev.deadzone.core.compound

import com.mongodb.client.model.Filters
import com.mongodb.kotlin.client.coroutine.MongoCollection
import dev.deadzone.core.data.runMongoCatching
import dev.deadzone.core.model.game.data.GameResources
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
}
