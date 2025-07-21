package dev.deadzone.core.auth

import dev.deadzone.core.data.user.UserData

interface AuthProvider {
    fun getPlayerById(userId: String): UserData
}
