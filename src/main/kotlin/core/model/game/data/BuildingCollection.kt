package dev.deadzone.core.model.game.data

import kotlinx.serialization.Serializable
import dev.deadzone.core.model.game.data.Building

@Serializable
data class BuildingCollection(
    val list: List<Building> = listOf()
) {
    companion object {
        fun dummy(): List<Building> {
            return listOf(
                Building.bed(200, 200),
                Building.bed(400, 400),
                Building.bed(600, 600),
                Building.bed(100, 100),
                Building.bed(50, 50),
            )
        }
    }
}
