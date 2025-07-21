package dev.deadzone.core.data.user

interface PlayerRegistry {
    val playersInfo: List<PlayerInfo>
    fun getPlayerById(playerId: String): PlayerInfo
}
