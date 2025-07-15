package dev.deadzone.api.message.db

import kotlinx.serialization.Serializable

@Serializable
data class LoadObjectsOutput(
    val objects: List<BigDBObject> = listOf()
) {
    companion object {
        fun playerObjects(): LoadObjectsOutput {
            return LoadObjectsOutput()
        }

        fun neighborHistory(): LoadObjectsOutput {
            return LoadObjectsOutput()
        }

        fun inventory(): LoadObjectsOutput {
            return LoadObjectsOutput()
        }
    }
}
