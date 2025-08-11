package dev.deadzone.core.survivor

import com.mongodb.client.model.Filters
import com.mongodb.client.model.Updates
import com.mongodb.kotlin.client.coroutine.MongoCollection
import dev.deadzone.core.model.game.data.Survivor
import dev.deadzone.data.collection.PlayerObjects
import kotlinx.coroutines.flow.firstOrNull

class SurvivorRepositoryMongo(val objCollection: MongoCollection<PlayerObjects>) : SurvivorRepository {
    /**
     * Get survivors of [playerId], returning an empty list if nothing is present.
     */
    override suspend fun getSurvivors(playerId: String): Result<List<Survivor>> {
        return try {
            val playerObj = objCollection
                .find(Filters.eq("playerId", playerId))
                .firstOrNull()

            if (playerObj == null) {
                Result.failure(NoSuchElementException("No player found with id=$playerId"))
            } else {
                Result.success(playerObj.survivors)
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun updateSurvivor(
        playerId: String,
        srvId: String,
        updateAction: suspend (Survivor) -> Survivor
    ): Result<Unit> {
        return try {
            val playerObj = objCollection
                .find(Filters.eq("playerId", playerId))
                .firstOrNull()

            if (playerObj == null) {
                Result.failure(NoSuchElementException("No player found with id=$playerId"))
            } else {
                val index = playerObj.survivors.indexOfFirst { it.id == srvId }
                if (index == -1) throw NoSuchElementException("Survivor for playerId=$playerId srvId=$srvId not found")

                val currentSurvivor = playerObj.survivors[index]
                val updatedSurvivor = updateAction(currentSurvivor)

                val path = "survivors.$index"
                val update = Updates.set(path, updatedSurvivor)

                objCollection.updateOne(Filters.eq("playerId", playerId), update)
                Result.success(Unit)
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
