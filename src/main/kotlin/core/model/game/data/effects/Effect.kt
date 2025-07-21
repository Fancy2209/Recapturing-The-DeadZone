package dev.deadzone.core.model.game.data.effects

import kotlinx.serialization.Serializable
import dev.deadzone.core.model.game.data.effects.EffectData
import dev.deadzone.core.model.game.data.TimerData
import java.io.ByteArrayOutputStream
import java.io.DataOutputStream

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
) {
    companion object {
        fun dummyEffectByteArray(): ByteArray {
            val output = ByteArrayOutputStream()
            val data = DataOutputStream(output)

            // Write header values required before effect list (optional placeholder):
            data.writeUTF("VacationMode") // _type
            data.writeUTF("vacation")  // _id
            data.writeByte(0)         // unused byte
            data.writeInt(0)          // _lockTime
            data.writeInt(0)          // _cooldownTime

            // Timer not present
            data.writeByte(0) // No _timer

            // Lockout timer not present
            data.writeByte(0) // No _lockoutTimer

            // Now the effect list
            val effects = listOf(
                EffectData(100u, 10.0), // BarricadeHealth
                EffectData(101u, 5.0),  // BarricadeCover
                EffectData(102u, 8.0),  // etc...
                EffectData(103u, 4.0)
            )
            data.writeByte(effects.size) // number of effects

            for (effect in effects) {
                data.writeInt(effect.type.toInt())   // short
                data.writeDouble(effect.value)    // float
            }

            // Optional itemId (not present)
            data.writeByte(0) // No itemId

            return output.toByteArray()
        }
    }
}
