package dev.deadzone.context

import dev.deadzone.core.auth.AuthProvider
import dev.deadzone.core.auth.SessionManager
import dev.deadzone.core.compound.CompoundService
import dev.deadzone.core.items.InventoryService
import dev.deadzone.core.survivor.SurvivorService
import dev.deadzone.data.collection.PlayerAccount
import dev.deadzone.data.db.BigDB
import dev.deadzone.socket.Connection
import dev.deadzone.socket.OnlinePlayerRegistry

/**
 * Holds the global server components. Only single instance of this class is needed in the server.
 */
data class ServerContext(
    val db: BigDB,
    val sessionManager: SessionManager,
    val onlinePlayerRegistry: OnlinePlayerRegistry,
    val authProvider: AuthProvider,
    val config: ServerConfig,
)

data class ServerConfig(
    val adminEnabled: Boolean,
    val useMongo: Boolean,
    val mongoUrl: String,
    val isProd: Boolean,
)

/**
 * A player-scoped data holder. This includes player's socket connection, metadata,
 * and the player's game data, which isn't directly, but found in various [PlayerService].
 */
data class PlayerContext(
    val playerId: String,
    val connection: Connection,
    val onlineSince: Long,
    val playerAccount: PlayerAccount,
    val services: PlayerServices
)

data class PlayerServices(
    val survivor: SurvivorService,
    val compound: CompoundService,
    val inventory: InventoryService
)
