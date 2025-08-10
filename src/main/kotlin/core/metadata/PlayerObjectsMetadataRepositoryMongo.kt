package dev.deadzone.core.metadata

import com.mongodb.client.model.Filters
import com.mongodb.client.model.Updates
import com.mongodb.kotlin.client.coroutine.MongoCollection
import dev.deadzone.data.collection.PlayerObjects
import dev.deadzone.utils.Logger
import kotlinx.coroutines.flow.firstOrNull

class PlayerObjectsMetadataRepositoryMongo(
    val plyObj: MongoCollection<PlayerObjects>
) : PlayerObjectsMetadataRepository {
    override suspend fun getPlayerFlags(playerId: String): ByteArray {
        val playerObj = plyObj.find(Filters.eq("playerId", playerId)).firstOrNull()
        return playerObj?.flags ?: byteArrayOf()
    }

    override suspend fun updatePlayerFlags(playerId: String, flags: ByteArray) {
        Logger.debug { "updatePlayerFlags: playerId=$playerId flags=$flags" }

        val path = "flags"
        val update = Updates.set(path, flags)

        plyObj.updateOne(Filters.eq("playerId", playerId), update)
    }
}
