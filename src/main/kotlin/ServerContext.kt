package dev.deadzone

import dev.deadzone.core.auth.AuthProvider
import dev.deadzone.core.auth.SessionManager
import dev.deadzone.data.db.BigDB
import dev.deadzone.socket.PlayerRegistry

data class ServerContext(
    val db: BigDB,
    val sessionManager: SessionManager,
    val playerRegistry: PlayerRegistry,
    val authProvider: AuthProvider,
    val config: ServerConfig,
)

data class ServerConfig(
    val adminEnabled: Boolean,
    val useMongo: Boolean,
    val mongoUrl: String,
    val isProd: Boolean,
)
