package dev.deadzone.api.message

import kotlinx.serialization.Serializable

@Serializable
data class AuthenticateOutput(
    val token: String = "",
    val userId: String = "",
    val showBranding: Boolean = false,
    val playerInsightState: String = "",
    val isSocialNetworkUser: Boolean = false,
    val isInstalledByPublishingNetwork: Boolean = false,
    val deprecated1: Boolean = false,
    val apiSecurity: String = "",
    val apiServerHosts: List<String> = listOf("")
) {
    companion object {
        fun dummy(): AuthenticateOutput {
            return AuthenticateOutput(
                token = "mock-auth-token-123456",
                userId = "user123",
                showBranding = false,
                playerInsightState = "",
                isSocialNetworkUser = false,
                isInstalledByPublishingNetwork = false,
                deprecated1 = false,
                apiSecurity = "",
                apiServerHosts = listOf("127.0.0.1:8080")
            )
        }
    }
}
