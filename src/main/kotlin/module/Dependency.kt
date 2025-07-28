package dev.deadzone.module

import dev.deadzone.core.data.BigDB
import kotlinx.serialization.json.Json

object Dependency {
    lateinit var database: BigDB
    lateinit var json: Json
}
