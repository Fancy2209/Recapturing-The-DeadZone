package dev.deadzone.user.model

import dev.deadzone.core.data.AdminData
import kotlinx.serialization.Serializable

/**
 * Contains frequently accessed metadata of a player. This is useful to get important
 * player's data without the need of loading the giant [dev.deadzone.data.collection.PlayerObjects] class.
 */
@Serializable
data class PlayerMetadata(
    val playerId: String, // reference to UserDocument
    val displayName: String,
    val playerSrvId: String,
    val leaderTitle: String,
    val level: Int,
    val xp: Int,
) {
    companion object {
        fun admin(): PlayerMetadata {
            return PlayerMetadata(
                playerId = AdminData.PLAYER_ID,
                displayName = AdminData.DISPLAY_NAME,
                playerSrvId = AdminData.PLAYER_SRV_ID,
                leaderTitle = AdminData.PLAYER_LEADER_TITLE,
                level = AdminData.PLAYER_LEVEL,
                xp = AdminData.PLAYER_EXP
            )
        }
    }
}
