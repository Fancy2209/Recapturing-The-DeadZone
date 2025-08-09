package dev.deadzone.data.db

import com.mongodb.kotlin.client.coroutine.MongoCollection
import dev.deadzone.data.collection.UserDocument
import dev.deadzone.core.auth.model.UserProfile
import dev.deadzone.core.model.game.data.HumanAppearance
import dev.deadzone.data.collection.Inventory
import dev.deadzone.data.collection.NeighborHistory
import dev.deadzone.data.collection.PlayerMetadata
import dev.deadzone.data.collection.PlayerObjects

/**
 * Representation of PlayerIO BigDB
 */
interface BigDB {
    suspend fun loadUserDocument(playerId: String): UserDocument?
    suspend fun loadPlayerMetadata(playerId: String): PlayerMetadata?
    suspend fun loadPlayerObjects(playerId: String): PlayerObjects?
    suspend fun loadNeighborHistory(playerId: String): NeighborHistory?
    suspend fun loadInventory(playerId: String): Inventory?

    suspend fun getUserDocumentCollection(): MongoCollection<UserDocument>

    /**
     * Create a user with the provided username and password.
     *
     * @return playerId (UUID) of the newly created user.
     */
    suspend fun createUser(username: String, password: String): String

    suspend fun doesUserExist(username: String): Boolean

    suspend fun getUserDocByUsername(username: String): UserDocument?

    suspend fun getUserDocByPlayerId(playerId: String): UserDocument?

    suspend fun getPlayerIdOfUsername(username: String): String?

    suspend fun getProfileOfPlayerId(playerId: String): UserProfile?

    suspend fun saveSurvivorAppearance(playerId: String, srvId: String, newAppearance: HumanAppearance)

    suspend fun updatePlayerFlags(playerId: String, flags: ByteArray)

    /**
     * Verify credentials of the given username and password
     *
     * @return playerId for the corresponding username if success
     */
    suspend fun verifyCredentials(username: String, password: String): String?
}
