package dev.deadzone.api.message.server

import kotlinx.serialization.Serializable

@Serializable
data class ServerEndpoint(
    val address: String = "",
    val port: Int = 0,
) {
    companion object {
        fun dummy(): ServerEndpoint {
            return ServerEndpoint(
                address = "127.0.0.1",
                port = 7777
            )
        }
    }
}
