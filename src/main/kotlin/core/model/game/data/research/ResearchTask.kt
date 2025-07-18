package dev.deadzone.core.model.game.data.research

import kotlinx.serialization.Serializable

@Serializable
data class ResearchTask(
    val active: List<ResearchTask>?,
    val levels: Map<String, Int>?
)
