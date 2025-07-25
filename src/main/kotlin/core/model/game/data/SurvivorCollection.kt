package dev.deadzone.core.model.game.data

import kotlinx.serialization.Serializable
import dev.deadzone.core.model.game.data.Survivor

@Serializable
data class SurvivorCollection(
    val list: List<Survivor> = listOf()
) {
    companion object {
        fun dummy(): List<Survivor> {
            return listOf(
                Survivor.playerM(),
                Survivor.fighterM(),
                Survivor.reconF(),
            )
        }
    }
}
