package dev.deadzone.context

import dev.deadzone.core.auth.AuthProvider
import dev.deadzone.core.auth.SessionManager
import dev.deadzone.data.db.BigDB
import dev.deadzone.socket.OnlinePlayerRegistry

/**
 * Holds the global server components. Only single instance of this class is needed in the server.
 */
data class ServerContext(
    val db: BigDB,
    val sessionManager: SessionManager,
    val onlinePlayerRegistry: OnlinePlayerRegistry,
    val authProvider: AuthProvider,
    val playerContextTracker: PlayerContextTracker,
    val config: ServerConfig,
)

data class ServerConfig(
    val adminEnabled: Boolean,
    val useMongo: Boolean,
    val mongoUrl: String,
    val isProd: Boolean,
)
