package dev.deadzone.core.auth

import dev.deadzone.core.auth.model.PlayerSession

interface AuthProvider {
    /**
     * Register a new account with [username] and [password].
     *
     * @return [PlayerSession] of the newly created account for further authentication.
     */
    suspend fun register(username: String, password: String): PlayerSession

    /**
     * Login with [username] and [password].
     *
     * @return [PlayerSession] which is used for further authentication, null if login is failed.
     */
    suspend fun login(username: String, password: String): PlayerSession?

    /**
     * Check whether a user with [username] exists.
     */
    suspend fun doesUserExist(username: String): Boolean

    suspend fun createAdminAccount(): PlayerSession
}
