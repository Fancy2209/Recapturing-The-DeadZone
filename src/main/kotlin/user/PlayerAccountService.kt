package dev.deadzone.user

import dev.deadzone.core.auth.model.UserProfile
import dev.deadzone.data.collection.PlayerAccount

/**
 * Central registry of player's account ([PlayerAccount]).
 */
class PlayerAccountService(
    private val playerAccountRepository: PlayerAccountRepository
) {

    private val cache = mutableMapOf<String, PlayerAccount>()

    suspend fun doesUserExist(username: String): Boolean {
        return playerAccountRepository.doesUserExist(username)
    }

    suspend fun getUserDocByUsername(username: String): PlayerAccount? {
        return playerAccountRepository.getUserDocByUsername(username)?.also { cache[it.playerId] = it }
    }

    suspend fun getUserDocByPlayerId(playerId: String): PlayerAccount? {
        return cache[playerId] ?: playerAccountRepository.getUserDocByPlayerId(playerId)?.also { cache[playerId] = it }
    }

    suspend fun getPlayerIdOfUsername(username: String): String? {
        return getUserDocByUsername(username)?.playerId
    }

    suspend fun getProfileOfPlayerId(playerId: String): UserProfile? {
        return getUserDocByPlayerId(playerId)?.profile
    }
}
