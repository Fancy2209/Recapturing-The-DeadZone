package dev.deadzone.socket.handler.save.compound

import dev.deadzone.socket.handler.save.BaseResponse
import kotlinx.serialization.Serializable

@Serializable
data class SaveBuildingResponse(
    val success: Boolean = true,
    val x: Int,
    val y: Int,
    val r: Int,
): BaseResponse()
