package dev.deadzone.core.auth

import dev.deadzone.core.auth.model.PlayerSession

interface AuthProvider {
    /**
     * Register a new account with [username] and [password].
     *
     * @return [PlayerSession] which includes newly created playerId and a token.
     */
    suspend fun register(username: String, password: String): PlayerSession

    /**
     * Login with [username] and [password].
     *
     * @return [PlayerSession] which is used for further authentication.
     */
    suspend fun login(username: String, password: String): PlayerSession

    /**
     * Check whether a user with [username] exists.
     */
    suspend fun doesUserExist(username: String): Boolean
}
