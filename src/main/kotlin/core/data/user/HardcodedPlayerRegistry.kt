package dev.deadzone.core.data.user

import dev.deadzone.core.data.HardcodedData

object HardcodedPlayerRegistry : PlayerRegistry {
    override val playersInfo = listOf(
        PlayerInfo(
            userId = HardcodedData.USER_ID,
            nickname = HardcodedData.NICKNAME,
            isAdmin = true
        ),
    )

    override fun getPlayerById(userId: String): PlayerInfo {
        return playersInfo.find { it.userId == userId } ?: playersInfo[0]
    }
}
