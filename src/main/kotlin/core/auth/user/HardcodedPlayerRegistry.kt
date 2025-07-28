package core.auth.user

import dev.deadzone.core.data.HardcodedData

object HardcodedPlayerRegistry : PlayerRegistry {
    override val playersInfo = listOf(
        PlayerInfo(
            playerId = HardcodedData.PLAYER_ID,
            nickname = HardcodedData.NICKNAME,
            isAdmin = true
        ),
    )

    override fun getPlayerById(playerId: String): PlayerInfo {
        return playersInfo.find { it.playerId == playerId } ?: playersInfo[0]
    }
}
