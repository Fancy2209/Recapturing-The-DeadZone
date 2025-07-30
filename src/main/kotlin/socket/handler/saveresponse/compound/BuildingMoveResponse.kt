package dev.deadzone.socket.handler.saveresponse.compound

import dev.deadzone.socket.handler.saveresponse.BaseResponse
import kotlinx.serialization.Serializable

@Serializable
data class BuildingMoveResponse(
    val success: Boolean = true,
    val x: Int,
    val y: Int,
    val r: Int,
): BaseResponse()
