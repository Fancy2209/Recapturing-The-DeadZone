package dev.deadzone.data.collection

import kotlinx.serialization.Serializable

/**
 * Contains frequently accessed metadata of a player. This is useful to get important
 * player's data without the need of loading the giant [PlayerObjects] class.
 */
@Serializable
data class PlayerMetadata(
    val playerId: String, // reference to UserDocument
    val displayName: String,
    val playerFlags: ByteArray,
    val playerSrvId: String,
    val leaderTitle: String,
    val level: Int,
    val xp: Int,
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as PlayerMetadata

        if (level != other.level) return false
        if (xp != other.xp) return false
        if (playerId != other.playerId) return false
        if (displayName != other.displayName) return false
        if (!playerFlags.contentEquals(other.playerFlags)) return false
        if (playerSrvId != other.playerSrvId) return false
        if (leaderTitle != other.leaderTitle) return false

        return true
    }

    override fun hashCode(): Int {
        var result = level
        result = 31 * result + xp
        result = 31 * result + playerId.hashCode()
        result = 31 * result + displayName.hashCode()
        result = 31 * result + playerFlags.contentHashCode()
        result = 31 * result + playerSrvId.hashCode()
        result = 31 * result + leaderTitle.hashCode()
        return result
    }
}
