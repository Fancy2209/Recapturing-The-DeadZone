package dev.deadzone.api.message.db

import kotlinx.serialization.Serializable

@Serializable
data class LoadObjectsOutput(
    val objects: List<BigDBObject> = listOf()
)
