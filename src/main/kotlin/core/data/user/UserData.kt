package dev.deadzone.core.data.user

/**
 * Server-level representation of player (not to be confused with game-level representation PlayerData)
 */
data class UserData(
    val userId: String,
    val nickname: String,
    val isAdmin: Boolean = false,
    val extra: Map<String, Any?> = emptyMap()
)
