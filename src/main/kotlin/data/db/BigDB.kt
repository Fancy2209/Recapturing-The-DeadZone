package dev.deadzone.data.db

import com.mongodb.kotlin.client.coroutine.MongoCollection
import dev.deadzone.data.collection.PlayerAccount
import dev.deadzone.core.model.game.data.HumanAppearance
import dev.deadzone.data.collection.Inventory
import dev.deadzone.data.collection.NeighborHistory
import dev.deadzone.user.model.PlayerMetadata
import dev.deadzone.data.collection.PlayerObjects

/**
 * Representation of PlayerIO BigDB
 */
interface BigDB {
    // each method load the corresponding collection
    suspend fun loadUserDocument(playerId: String): PlayerAccount?
    suspend fun loadPlayerObjects(playerId: String): PlayerObjects?
    suspend fun loadNeighborHistory(playerId: String): NeighborHistory?
    suspend fun loadInventory(playerId: String): Inventory?

    suspend fun getUserDocumentCollection(): MongoCollection<PlayerAccount>

    /**
     * Create a user with the provided username and password.
     *
     * This method is defined in BigDB because it require access to all 5 collections.
     *
     * @return playerId (UUID) of the newly created user.
     */
    suspend fun createUser(username: String, password: String): String

    suspend fun updatePlayerFlags(playerId: String, flags: ByteArray)
}
