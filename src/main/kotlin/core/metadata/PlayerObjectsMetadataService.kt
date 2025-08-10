package dev.deadzone.core.metadata

import dev.deadzone.core.PlayerService

class PlayerObjectsMetadataService(
    private val playerObjectsMetadataRepository: PlayerObjectsMetadataRepository
) : PlayerService {
    private val flags: ByteArray = byteArrayOf()
    private lateinit var playerId: String

    suspend fun updatePlayerFlags(flags: ByteArray) {
        playerObjectsMetadataRepository.updatePlayerFlags(playerId, flags)
    }

    override suspend fun init(playerId: String): Result<Unit> {
        val flags_ = playerObjectsMetadataRepository.getPlayerFlags(playerId)
        return if (flags_.isEmpty()) {
            Result.failure(IllegalStateException("PlayerFlags for playerId=$playerId is empty"))
        } else {
            Result.success(Unit)
        }
    }
}
