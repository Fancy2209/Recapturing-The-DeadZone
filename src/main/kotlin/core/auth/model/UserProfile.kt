package dev.deadzone.core.auth.model

import dev.deadzone.core.data.DummyData
import io.ktor.util.date.getTimeMillis
import kotlinx.serialization.Serializable

/**
 * @property playerId reference to `playerId` in [UserDocument]
 * @property email unused
 * @property displayName nickname or display name in-game
 * @property avatarUrl in-game avatar display and website
 * @property createdAt account creation date
 * @property lastLogin date for last login, may be same with last online in-game
 * @property countryCode country code
 * @property friends unused
 * @property enemies unused
 */
@Serializable
data class UserProfile(
    val playerId: String,
    val email: String = "",
    val displayName: String,
    val avatarUrl: String,
    val createdAt: Long = getTimeMillis(),
    val lastLogin: Long = getTimeMillis(),
    val countryCode: String? = null,
    val friends: Set<UserProfile> = emptySet(),
    val enemies: Set<UserProfile> = emptySet(),
) {
    companion object {
        fun admin(): UserProfile {
            return UserProfile(
                playerId = DummyData.PLAYER_ID,
                email = DummyData.EMAIL,
                displayName = DummyData.DISPLAY_NAME,
                avatarUrl = DummyData.AVATAR_URL,
                countryCode = DummyData.COUNTRY_CODE,
            )
        }
    }
}
