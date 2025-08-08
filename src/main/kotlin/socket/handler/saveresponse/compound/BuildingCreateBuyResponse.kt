package dev.deadzone.socket.handler.saveresponse.compound

import kotlinx.serialization.Serializable

@Serializable
data class BuildingCreateBuyResponse(
    val success: Boolean,
    // likely level pts gained after creating the building
    val levelPts: Int? = null,
)
