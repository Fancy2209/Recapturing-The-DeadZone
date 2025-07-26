package dev.deadzone.socket.handler.save

import dev.deadzone.core.model.game.data.skills.SkillState

/**
 * Every save message expects coins and skills update from server
 *
 * @property coins also known as CASH or FUEL in the game
 * @property skills not sure what is this
 */
open class BaseResponse {
    val coins: Int? = null
    val skills: List<SkillState>? = null
}
