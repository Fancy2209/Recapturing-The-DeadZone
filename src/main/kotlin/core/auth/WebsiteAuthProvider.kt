package dev.deadzone.core.auth

import dev.deadzone.core.auth.model.PlayerSession
import dev.deadzone.core.data.BigDB

class WebsiteAuthProvider(
    private val db: BigDB,
    private val sessionManager: SessionManager
) : AuthProvider {
    override suspend fun register(username: String, password: String): PlayerSession {
        val pid = db.createUser(username, password)
        return sessionManager.create(playerId = pid)
    }

    override suspend fun login(username: String, password: String): PlayerSession? {
        val pid = db.verifyCredentials(username, password) ?: return null
        return sessionManager.create(pid)
    }

    override suspend fun doesUserExist(username: String): Boolean {
        return db.doesUserExist(username)
    }

    override suspend fun createAdminAccount(): PlayerSession {
        val adminpid = db.createAdminAccount()
        return sessionManager.create(adminpid)
    }
}
