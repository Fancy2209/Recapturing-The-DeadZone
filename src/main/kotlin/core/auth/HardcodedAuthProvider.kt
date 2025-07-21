package dev.deadzone.core.auth

import dev.deadzone.core.data.HardcodedData
import dev.deadzone.core.data.user.PlayerData

object HardcodedAuthProvider : AuthProvider {
    private val users = listOf(
        PlayerData(
            userId = HardcodedData.USER_ID,
            nickname = HardcodedData.NICKNAME,
            isAdmin = true
        ),
    )

    override fun getPlayerById(userId: String): PlayerData {
        return users.find { it.userId == userId } ?: users[0]
    }
}
