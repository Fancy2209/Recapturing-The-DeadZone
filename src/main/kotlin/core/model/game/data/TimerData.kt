package dev.deadzone.core.model.game.data

import io.ktor.util.date.getTimeMillis
import kotlinx.serialization.Serializable

@Serializable
data class TimerData(
    val start: Long,
    val length: Long,
    val data: Map<String, Double>?
) {
    companion object {
        fun dummy(): TimerData {
            return TimerData(
                start = 1000,
                length = 1_000_000,
                data = mapOf()
            )
        }

        fun hasEnded(): TimerData {
            return TimerData(
                start = getTimeMillis() - (5 * 60 * 1000), // 5 mins ago
                length = 1,                                // 1 sec duration
                data = mapOf()
            )
        }
    }
}
