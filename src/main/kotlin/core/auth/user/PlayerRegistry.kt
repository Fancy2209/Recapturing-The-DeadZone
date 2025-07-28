package core.auth.user

interface PlayerRegistry {
    val playersInfo: List<PlayerInfo>
    fun getPlayerById(playerId: String): PlayerInfo
}
