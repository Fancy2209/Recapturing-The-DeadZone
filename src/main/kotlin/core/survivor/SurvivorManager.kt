package dev.deadzone.core.survivor

import dev.deadzone.core.model.game.data.Survivor
import dev.deadzone.core.model.game.data.SurvivorCollection
import dev.deadzone.module.Logger

class SurvivorManager(private val survivors: List<Survivor>) {
    fun getSurvivorById(srvId: String?): Survivor? {
        val result = survivors.find { it.id == srvId }
        if (result == null) {
            Logger.warn { "Couldn't find survivor of id=$srvId in list of: $survivors" }
        }
        return result
    }
}
