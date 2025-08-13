package dev.deadzone.core.model.game.data

import io.ktor.util.date.*
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonElement
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

@Serializable
data class TimerData(
    val start: Long, // epoch millis
    val length: Long, // length in seconds!
    /**
     * use mapOf().toJsonElement()
     */
    val data: Map<String, JsonElement>? // this depends on each response. e.g., building upgrade need level
) {
    companion object {
        fun runForDuration(
            duration: Duration,
            data: Map<String, JsonElement>? = emptyMap()
        ): TimerData {
            return TimerData(
                start = getTimeMillis(),
                length = duration.inWholeSeconds,
                data = data
            )
        }
    }
}

fun TimerData.hasEnded(): Boolean {
    return getTimeMillis() >= this.start + this.length.seconds.inWholeMilliseconds
}
