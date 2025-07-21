package dev.deadzone.core.auth

import dev.deadzone.core.data.user.PlayerData

interface AuthProvider {
    fun getPlayerById(userId: String): PlayerData
}
