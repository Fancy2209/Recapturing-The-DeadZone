package dev.deadzone.socket.handler.save.compound

import dev.deadzone.core.model.game.data.skills.SkillState
import kotlinx.serialization.Serializable

@Serializable
data class SaveBuildingResponse(
    val success: Boolean = true,
    val x: Int,
    val y: Int,
    val r: Int,
    val coins: Int? = null, // used in NetworkMessage.SEND_RESPONSE, interpreted as CASH
    val skills: Map<String, SkillState>? = null, // used in NetworkMessage.SEND_RESPONSE
)
