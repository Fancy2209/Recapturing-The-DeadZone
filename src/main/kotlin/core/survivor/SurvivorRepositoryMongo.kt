package dev.deadzone.core.survivor

import com.mongodb.client.model.Filters
import com.mongodb.kotlin.client.coroutine.MongoCollection
import dev.deadzone.data.collection.PlayerAccount
import dev.deadzone.core.model.game.data.Survivor
import kotlinx.coroutines.flow.firstOrNull
import kotlin.collections.emptyList

class SurvivorRepositoryMongo(val udocs: MongoCollection<PlayerAccount>) : SurvivorRepository {
    /**
     * Get survivors of [playerId], returning an empty list if nothing is present.
     */
    override suspend fun getSurvivorsOfPlayerId(playerId: String): List<Survivor> {
        val udoc = udocs
            .find(Filters.eq("playerId", playerId))
            .firstOrNull()

        return udoc?.playerSave?.playerObjects?.survivors ?: emptyList()
    }
}
