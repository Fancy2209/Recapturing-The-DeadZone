package dev.deadzone.data.collection

import dev.deadzone.core.model.network.RemotePlayerData

/**
 * Neighbor history table
 */
data class NeighborHistory(
    val playerId: String, // reference to UserDocument
    val map: Map<String, RemotePlayerData>? = emptyMap()
)
