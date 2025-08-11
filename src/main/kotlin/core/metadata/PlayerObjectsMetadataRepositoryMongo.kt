package dev.deadzone.core.metadata

import com.mongodb.client.model.Filters
import com.mongodb.client.model.Updates
import com.mongodb.kotlin.client.coroutine.MongoCollection
import dev.deadzone.data.collection.PlayerObjects
import kotlinx.coroutines.flow.firstOrNull

class PlayerObjectsMetadataRepositoryMongo(
    private val objCollection: MongoCollection<PlayerObjects>
) : PlayerObjectsMetadataRepository {

    override suspend fun getPlayerFlags(playerId: String): Result<ByteArray> {
        return try {
            val playerObj = objCollection.find(Filters.eq("playerId", playerId)).firstOrNull()
            if (playerObj == null ) {
                Result.failure(NoSuchElementException("No player found with id=$playerId"))
            } else {
                Result.success(playerObj.flags)
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun updatePlayerFlags(playerId: String, flags: ByteArray): Result<Unit> {
        return try {
            val update = Updates.set("flags", flags)
            val result = objCollection.updateOne(Filters.eq("playerId", playerId), update)

            if (result.matchedCount == 0L) {
                Result.failure(NoSuchElementException("No player found with id=$playerId"))
            } else {
                Result.success(Unit)
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getPlayerNickname(playerId: String): Result<String?> {
        return try {
            val playerObj = objCollection.find(Filters.eq("playerId", playerId)).firstOrNull()
            if (playerObj == null) {
                Result.failure(NoSuchElementException("No player found with id=$playerId"))
            } else {
                Result.success(playerObj.nickname)
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun updatePlayerNickname(playerId: String, nickname: String): Result<Unit> {
        return try {
            val update = Updates.set("nickname", nickname)
            val result = objCollection.updateOne(Filters.eq("playerId", playerId), update)

            if (result.matchedCount == 0L) {
                Result.failure(NoSuchElementException("No player found with id=$playerId"))
            } else {
                Result.success(Unit)
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
