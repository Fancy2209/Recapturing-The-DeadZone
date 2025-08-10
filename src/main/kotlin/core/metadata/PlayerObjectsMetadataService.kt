package dev.deadzone.core.metadata

import dev.deadzone.core.PlayerService

class PlayerObjectsMetadataService(
    private val playerObjectsMetadataRepository: PlayerObjectsMetadataRepository
) : PlayerService {
    private var flags: ByteArray = byteArrayOf()
    private var nickname: String = ""
    private lateinit var playerId: String

    suspend fun updatePlayerFlags(flags: ByteArray) {
        playerObjectsMetadataRepository.updatePlayerFlags(playerId, flags)
        this.flags = flags
    }

    suspend fun updatePlayerNickname(nickname: String) {
        playerObjectsMetadataRepository.updatePlayerNickname(playerId, nickname)
        this.nickname = nickname
    }

    fun getPlayerFlags() = flags
    fun getPlayerNickname() = nickname

    override suspend fun init(playerId: String): Result<Unit> {
        this.playerId = playerId
        val flags_ = playerObjectsMetadataRepository.getPlayerFlags(playerId)
        val nickname_ = playerObjectsMetadataRepository.getPlayerNickname(playerId)
        flags = flags_
        nickname = nickname_ ?: ""
        return if (flags_.isEmpty()) {
            Result.failure(IllegalStateException("Failure during init of PlayerObjectsMetadataService for playerId=$playerId, flags is empty"))
        } else {
            Result.success(Unit)
        }
    }
}
