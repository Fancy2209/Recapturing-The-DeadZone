package dev.deadzone.core.model.game.data

import kotlinx.serialization.Serializable
import dev.deadzone.core.model.game.data.Building

@Serializable
data class BuildingCollection(
    val list: List<Building> = listOf()
) {
    companion object {
        fun dummy(l: List<Building>): List<Building> {
            return listOf(
                Building.bed(25, 40),
            ) + l
        }
    }
}
