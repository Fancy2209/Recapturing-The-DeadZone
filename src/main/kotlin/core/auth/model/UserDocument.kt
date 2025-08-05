package dev.deadzone.core.auth.model

import dev.deadzone.core.data.AdminData
import kotlinx.serialization.Serializable

/**
 * Database-level representation of a user data
 *
 * @property playerId internal ID, also known as userId in PIO login
 * @property hashedPassword using particular hash system
 */
@Serializable
data class UserDocument(
    val playerId: String,
    val hashedPassword: String,
    val profile: UserProfile,
    val playerSave: PlayerSave,
    val metadata: ServerMetadata,
) {
    companion object {
        fun admin(): UserDocument {
            return UserDocument(
                playerId = AdminData.PLAYER_ID,
                hashedPassword = AdminData.PASSWORD,
                profile = UserProfile.admin(),
                playerSave = PlayerSave.admin(),
                metadata = ServerMetadata()
            )
        }
    }
}
