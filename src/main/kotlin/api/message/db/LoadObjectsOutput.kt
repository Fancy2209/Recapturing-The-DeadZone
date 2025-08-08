package dev.deadzone.api.message.db

import dev.deadzone.utils.Converter
import kotlinx.serialization.Serializable

@Serializable
data class LoadObjectsOutput(
    val objects: List<BigDBObject> = listOf()
) {
    companion object {
        inline fun <reified T : Any> fromData(obj: T): BigDBObject {
            return Converter.toBigDBObject(obj = obj)
        }
    }
}
