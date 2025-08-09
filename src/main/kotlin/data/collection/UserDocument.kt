package dev.deadzone.data.collection

import dev.deadzone.core.auth.model.PlayerSave
import dev.deadzone.core.auth.model.ServerMetadata
import dev.deadzone.core.auth.model.UserProfile
import dev.deadzone.core.data.AdminData
import kotlinx.serialization.Serializable

/**
 * Database-level representation of a user data
 *
 * @property playerId internal ID, also known as userId in PIO login.
 * This is also used to uniquely identify a user document.
 *
 * @property hashedPassword using particular hash system
 */
@Serializable
data class UserDocument(
    val playerId: String, // referenced by other collections
    val hashedPassword: String,
    val profile: UserProfile,
    val metadata: ServerMetadata,
) {
    companion object {
        fun admin(): UserDocument {
            return UserDocument(
                playerId = AdminData.PLAYER_ID,
                hashedPassword = AdminData.PASSWORD,
                profile = UserProfile.Companion.admin(),
                metadata = ServerMetadata()
            )
        }
    }
}