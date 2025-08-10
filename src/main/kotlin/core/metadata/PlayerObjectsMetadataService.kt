package dev.deadzone.core.metadata

import dev.deadzone.core.PlayerService

class PlayerObjectsMetadataService(
    private val playerObjectsMetadataRepository: PlayerObjectsMetadataRepository
) : PlayerService {
    private var flags: ByteArray = byteArrayOf()
    private lateinit var playerId: String

    suspend fun updatePlayerFlags(flags: ByteArray) {
        playerObjectsMetadataRepository.updatePlayerFlags(playerId, flags)
        this.flags = flags
    }

    fun getPlayerFlags() = flags

    override suspend fun init(playerId: String): Result<Unit> {
        this.playerId = playerId
        val flags_ = playerObjectsMetadataRepository.getPlayerFlags(playerId)
        flags = flags_
        return if (flags_.isEmpty()) {
            Result.failure(IllegalStateException("Failure during init of PlayerObjectsMetadataService for playerId=$playerId is empty"))
        } else {
            Result.success(Unit)
        }
    }
}
