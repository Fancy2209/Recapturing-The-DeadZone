package dev.deadzone.core.compound

import com.mongodb.client.model.Filters
import com.mongodb.kotlin.client.coroutine.MongoCollection
import dev.deadzone.core.model.game.data.GameResources
import dev.deadzone.data.collection.PlayerObjects
import kotlinx.coroutines.flow.firstOrNull

class CompoundRepositoryMongo(val plyObj: MongoCollection<PlayerObjects>) : CompoundRepository {
    override suspend fun getGameResources(playerId: String): GameResources? {
        val playerObj = plyObj.find(Filters.eq("playerId", playerId)).firstOrNull()
            ?: throw IllegalArgumentException("PlayerObjects for playerId=$playerId not found")

        return playerObj.resources
    }
}
