package dev.deadzone.core.survivor

import com.mongodb.client.model.Filters
import com.mongodb.client.model.Updates
import com.mongodb.kotlin.client.coroutine.MongoCollection
import dev.deadzone.core.model.game.data.Survivor
import dev.deadzone.data.collection.PlayerObjects
import kotlinx.coroutines.flow.firstOrNull

class SurvivorRepositoryMongo(val plyObj: MongoCollection<PlayerObjects>) : SurvivorRepository {
    /**
     * Get survivors of [playerId], returning an empty list if nothing is present.
     */
    override suspend fun getSurvivorsOfPlayerId(playerId: String): List<Survivor> {
        return plyObj
            .find(Filters.eq("playerId", playerId))
            .firstOrNull()
            ?.survivors ?: emptyList()
    }

    override suspend fun updateSurvivor(
        playerId: String,
        srvId: String,
        updateAction: suspend (Survivor) -> Survivor
    ): Result<Unit> {
        return runCatching {
            val playerObj = plyObj.find(Filters.eq("playerId", playerId)).firstOrNull()
                ?: throw IllegalArgumentException("PlayerObjects for playerId=$playerId not found")

            val index = playerObj.survivors.indexOfFirst { it.id == srvId }
            if (index == -1) throw IllegalArgumentException("Survivor for playerId=$playerId srvId=$srvId not found")

            val currentSurvivor = playerObj.survivors[index]
            val updatedSurvivor = updateAction(currentSurvivor)

            val path = "survivors.$index"
            val update = Updates.set(path, updatedSurvivor)

            plyObj.updateOne(Filters.eq("playerId", playerId), update)
        }
    }
}
