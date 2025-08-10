package dev.deadzone.utils

import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.boolean
import kotlinx.serialization.json.booleanOrNull
import kotlinx.serialization.json.double
import kotlinx.serialization.json.doubleOrNull
import kotlinx.serialization.json.int
import kotlinx.serialization.json.intOrNull
import kotlinx.serialization.json.long
import kotlinx.serialization.json.longOrNull
import kotlin.collections.map
import kotlin.collections.mapValues

fun parseJsonToMap(json: String): Map<String, Any?> {
    return try {
        val parsed = Json.decodeFromString<JsonObject>(json)
        parsed.mapValues { (_, v) -> parseJsonElement(v) }
    } catch (_: Exception) {
        emptyMap()
    }
}

fun parseJsonElement(el: JsonElement): Any? = when (el) {
    is JsonPrimitive -> {
        when {
            el.isString -> el.content
            el.booleanOrNull != null -> el.boolean
            el.intOrNull != null -> el.int
            el.longOrNull != null -> el.long
            el.doubleOrNull != null -> el.double
            else -> el.content
        }
    }

    is JsonObject -> el.mapValues { parseJsonElement(it.value) }
    is JsonArray -> el.map { parseJsonElement(it) }
}
