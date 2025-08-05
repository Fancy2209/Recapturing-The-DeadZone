package dev.deadzone.core.auth

import dev.deadzone.core.auth.model.PlayerSession
import dev.deadzone.core.data.AdminData
import io.ktor.util.date.getTimeMillis

/**
 * Dummy auth provider, typically used for dev/testing purpose.
 * Any operation always succeed.
 */
object DummyAuthProvider : AuthProvider {
    override suspend fun register(
        username: String,
        password: String
    ): PlayerSession {
        return PlayerSession(
            playerId = AdminData.PLAYER_ID,
            token = AdminData.TOKEN,
            issuedAt = getTimeMillis(),
            expiresAt = getTimeMillis() + 60 * 1000 * 5 // 5 minutes
        )
    }

    override suspend fun login(
        username: String,
        password: String
    ): PlayerSession {
        return PlayerSession(
            playerId = AdminData.PLAYER_ID,
            token = AdminData.TOKEN,
            issuedAt = getTimeMillis(),
            expiresAt = getTimeMillis() + 60 * 1000 * 5 // 5 minutes
        )
    }

    override suspend fun doesUserExist(username: String): Boolean = true
}
