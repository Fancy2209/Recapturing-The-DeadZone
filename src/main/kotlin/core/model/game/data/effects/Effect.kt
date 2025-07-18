package dev.deadzone.core.model.game.data.effects

import kotlinx.serialization.Serializable
import dev.deadzone.core.model.game.data.effects.EffectData
import dev.deadzone.core.model.game.data.TimerData

@Serializable
data class Effect(
    val raw: ByteArray = byteArrayOf(),
    val type: String,
    val id: String,
    val lockTime: Int,
    val cooldownTime: Int,
    val started: Boolean = false,
    val timer: TimerData?,
    val lockoutTimer: TimerData?,
    val effectList: List<EffectData> = listOf(),
    val itemId: String?
)
