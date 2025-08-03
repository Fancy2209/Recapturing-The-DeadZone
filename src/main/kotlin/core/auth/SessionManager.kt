package dev.deadzone.core.auth

import dev.deadzone.core.auth.model.PlayerSession
import io.ktor.util.date.getTimeMillis
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import java.util.UUID
import java.util.concurrent.ConcurrentHashMap

/**
 * Handles short-lived authentication sessions between web login and game socket connection.
 *
 * ### Flow:
 * - After a successful website login, this class issues a session token for the player.
 * - The client uses this token during API 13 and API 601.
 * - Later, when the game client connects to the socket server, it sends the `playerId`.
 * - If a valid session exists for that `playerId` (based on token and expiry), the socket is accepted.
 */
class SessionManager {
    private val sessions = ConcurrentHashMap<String, PlayerSession>()
    private val cleanupInterval = 60_000L // 60 seconds
    private val cleanupJob = Job()
    private val scope = CoroutineScope(Dispatchers.IO + cleanupJob)

    init {
        scope.launch {
            while (isActive) {
                cleanupExpiredSessions()
                delay(cleanupInterval)
            }
        }
    }

    fun create(playerId: String): PlayerSession {
        val now = getTimeMillis()

        val session = PlayerSession(
            playerId = playerId,
            token = UUID.randomUUID().toString(),
            issuedAt = now,
            expiresAt = now + 2 * 60 * 1000 // 2 minutes
        )

        sessions[playerId] = session
        return session
    }

    fun verify(playerId: String, token: String): Boolean {
        val session = sessions[playerId] ?: return false
        val now = getTimeMillis()

        if (now >= session.expiresAt) {
            sessions.remove(playerId)
            return false
        }

        return session.token == token
    }

    fun getSession(playerId: String): PlayerSession? {
        val session = sessions[playerId] ?: return null
        val now = getTimeMillis()

        return if (now >= session.expiresAt) {
            sessions.remove(playerId)
            null
        } else {
            session
        }
    }

    fun invalidate(playerId: String) {
        sessions.remove(playerId)
    }

    fun cleanupExpiredSessions() {
        val now = getTimeMillis()
        val expiredKeys = sessions.filterValues { it.expiresAt <= now }.keys
        expiredKeys.forEach { sessions.remove(it) }
    }

    /**
     * Shutdown [cleanupExpiredSessions] task.
     */
    fun shutdown() {
        cleanupJob.cancel()
    }
}
