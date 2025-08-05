package dev.deadzone.core.data

import dev.deadzone.core.auth.model.UserDocument

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

    /**
     * Verify credentials of the given username and password
     *
     * @return playerId for the corresponding username if success
     */
    suspend fun verifyCredentials(username: String, password: String): String?
}
