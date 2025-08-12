package dev.deadzone.user

import dev.deadzone.core.auth.model.UserProfile
import dev.deadzone.data.collection.PlayerAccount

interface PlayerAccountRepository {
    suspend fun doesUserExist(username: String): Boolean

    suspend fun getUserDocByUsername(username: String): PlayerAccount?

    suspend fun getUserDocByPlayerId(playerId: String): PlayerAccount?

    suspend fun getPlayerIdOfUsername(username: String): String?

    suspend fun getProfileOfPlayerId(playerId: String): UserProfile?

    suspend fun updatePlayerAccount(playerId: String, account: PlayerAccount)

    suspend fun updateLastLogin(playerId: String, lastLogin: Long)

    /**
     * Verify credentials of the given username and password
     *
     * @return playerId for the corresponding username if success
     */
    suspend fun verifyCredentials(username: String, password: String): String?
}
