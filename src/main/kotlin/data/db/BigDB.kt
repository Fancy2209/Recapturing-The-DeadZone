package dev.deadzone.data.db

import dev.deadzone.core.auth.model.UserDocument
import dev.deadzone.core.auth.model.UserProfile

/**
 * Representation of PlayerIO BigDB
 *
 * Implement DB as you need. We have: [DocumentStoreDB].
 */
interface BigDB {
    /**
     * Create a user with the provided username and password.
     *
     * @return playerId (UUID) of the newly created user.
     */
    suspend fun createUser(username: String, password: String): String

    suspend fun doesUserExist(username: String): Boolean

    suspend fun getUserDocByUsername(username: String): UserDocument?

    suspend fun getPlayerIdOfUsername(username: String): String?

    suspend fun getProfileOfPlayerId(playerId: String): UserProfile?

    /**
     * Verify credentials of the given username and password
     *
     * @return playerId for the corresponding username if success
     */
    suspend fun verifyCredentials(username: String, password: String): String?
}
