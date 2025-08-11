package dev.deadzone.socket.handler.save.compound.response

import kotlinx.serialization.Serializable

@Serializable
data class BuildingCreateBuyResponse(
    val success: Boolean,
    // likely level pts gained after creating the building
    val levelPts: Int? = null,
)
