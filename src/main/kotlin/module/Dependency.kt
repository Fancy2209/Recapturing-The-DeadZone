package dev.deadzone.module

import dev.deadzone.core.data.GameResourceRegistry
import dev.deadzone.data.db.BigDB
import kotlinx.serialization.json.Json

object Dependency {
    lateinit var database: BigDB
    lateinit var json: Json
    lateinit var wsManager: WebsocketManager
    lateinit var gameResourceRegistry: GameResourceRegistry
}
