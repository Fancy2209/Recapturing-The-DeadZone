package dev.deadzone.core.auth.model

import dev.deadzone.core.model.data.user.UserProfile
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
)
