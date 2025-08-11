package dev.deadzone.core.survivor

import com.mongodb.client.model.Filters
import com.mongodb.client.model.Updates
import com.mongodb.kotlin.client.coroutine.MongoCollection
import dev.deadzone.core.data.runMongoCatching
import dev.deadzone.core.model.game.data.Survivor
import dev.deadzone.data.collection.PlayerObjects
import kotlinx.coroutines.flow.firstOrNull

class SurvivorRepositoryMongo(val objCollection: MongoCollection<PlayerObjects>) : SurvivorRepository {
    /**
     * Get survivors of [playerId], returning an empty list if nothing is present.
     */
    override suspend fun getSurvivors(playerId: String): Result<List<Survivor>> {
        return runMongoCatching("No player found with id=$playerId") {
            objCollection
                .find(Filters.eq("playerId", playerId))
                .firstOrNull()
                ?.survivors
        }
    }

    override suspend fun updateSurvivor(
        playerId: String,
        srvId: String,
        updateAction: suspend (Survivor) -> Survivor
    ): Result<Unit> {
        return runMongoCatching {
            val playerObj = objCollection
                .find(Filters.eq("playerId", playerId))
                .firstOrNull()
                ?: throw NoSuchElementException("No player found with id=$playerId")

            val index = playerObj.survivors.indexOfFirst { it.id == srvId }
            if (index == -1) throw NoSuchElementException("Survivor for playerId=$playerId srvId=$srvId not found")

            val currentSurvivor = playerObj.survivors[index]
            val updatedSurvivor = updateAction(currentSurvivor)

            val path = "survivors.$index"
            val update = Updates.set(path, updatedSurvivor)

            objCollection.updateOne(Filters.eq("playerId", playerId), update)
            Unit
        }
    }
}
