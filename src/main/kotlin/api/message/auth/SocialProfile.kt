package dev.deadzone.api.message.auth

import kotlinx.serialization.Serializable

@Serializable
data class SocialProfile(
    val userId: String = "",
    val displayName: String = "",
    val avatarUrl: String = "",
    val lastOnline: Long = 0,
    val countryCode: String = "",
    val userToken: String = "",
) {
    companion object {
        fun dummy(): SocialProfile {
            return SocialProfile(
                userId = "user123",
                displayName = "John Doe",
                avatarUrl = "http://example.com/avatar.png",
                lastOnline = 1622547800,
                countryCode = "US",
                userToken = "user-token-1",
            )
        }
    }
}
