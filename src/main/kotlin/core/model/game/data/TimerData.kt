package dev.deadzone.core.model.game.data

import io.ktor.util.date.getTimeMillis
import kotlinx.serialization.Serializable
import kotlin.time.Duration

@Serializable
data class TimerData(
    val start: Long,
    val length: Long, // this is in seconds!
    val data: Map<String, Double>? // this depends on each response. e.g., building upgrade need level
) {
    companion object {
        fun runForDuration(
            duration: Duration,
            data: Map<String, Double>? = emptyMap()
        ): TimerData {
            return TimerData(
                start = getTimeMillis(),
                length = duration.inWholeSeconds,
                data = data
            )
        }
    }
}
