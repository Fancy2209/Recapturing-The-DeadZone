package dev.deadzone.api.message.auth

import kotlinx.serialization.Serializable

@Serializable
data class SocialRefreshOutput(
    val myProfile: SocialProfile = SocialProfile(),
    val friends: List<SocialProfile> = listOf(),
    val blocked: String = "",
) {
    companion object {
        fun dummy(): SocialRefreshOutput {
            return SocialRefreshOutput(
                myProfile = SocialProfile.dummy(),
                friends = listOf(SocialProfile.dummy()),
                blocked = "No one"
            )
        }
    }
}
