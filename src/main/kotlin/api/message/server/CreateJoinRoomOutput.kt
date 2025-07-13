package dev.deadzone.api.message.client

import dev.deadzone.api.message.server.ServerEndpoint
import kotlinx.serialization.Serializable

@Serializable
data class CreateJoinRoomOutput(
    val roomId: String = "",
    val joinKey: String = "",
    val endpoints: ServerEndpoint,
) {
    companion object {
        fun dummy(): CreateJoinRoomOutput {
            return CreateJoinRoomOutput(
                roomId = "defaultRoomId",
                joinKey = "defaultJoinKey",
                endpoints = ServerEndpoint.dummy()
            )
        }
    }
}
