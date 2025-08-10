package dev.deadzone.core.metadata

/**
 * Repository for basic metadata fields in PlayerObjects like nickname, flags
 */
interface PlayerObjectsMetadataRepository {
    suspend fun getPlayerFlags(playerId: String): ByteArray
    suspend fun updatePlayerFlags(playerId: String, flags: ByteArray)
    suspend fun getPlayerNickname(playerId: String): String?
    suspend fun updatePlayerNickname(playerId: String, nickname: String)
}
