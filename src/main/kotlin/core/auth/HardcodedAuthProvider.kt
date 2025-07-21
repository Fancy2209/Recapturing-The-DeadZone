package dev.deadzone.core.auth

import dev.deadzone.core.data.HardcodedData
import dev.deadzone.core.data.user.UserData

object HardcodedAuthProvider : AuthProvider {
    private val users = listOf(
        UserData(
            userId = HardcodedData.USER_ID,
            nickname = HardcodedData.NICKNAME,
            isAdmin = true
        ),
    )

    override fun getPlayerById(userId: String): UserData {
        return users.find { it.userId == userId } ?: users[0]
    }
}
