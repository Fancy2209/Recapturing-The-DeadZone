package dev.deadzone.core.model.game.data

import kotlinx.serialization.Serializable

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

        fun simpleBase(): List<Building> {
            return listOf(
                Building(id = "bed", tx = 19, ty = 35, level = 5, rotation = 3),
                Building(id = "storage-ammunition", tx = 11, ty = 47, level = 5, rotation = 2),
                Building(id = "storage-cloth", tx = 12, ty = 38, level = 5, rotation = 0),
                Building(id = "storage-water", tx = 16, ty = 42, level = 5, rotation = 0),
                Building(id = "storage-metal", tx = 12, ty = 44, level = 5, rotation = 0),
                Building(id = "recycler", tx = 21, ty = 40, level = 9, rotation = 1),
                Building(id = "compound-barricade-small", tx = 17, ty = 62, level = 0, rotation = 3),
                Building(id = "compound-barricade-small", tx = 20, ty = 61, level = 0, rotation = 0),
                Building(id = "compound-barricade-small", tx = 15, ty = 59, level = 0, rotation = 2),
                Building(id = "compound-barricade-small", tx = 31, ty = 46, level = 0, rotation = 3),
                Building(id = "compound-barricade-small", tx = 33, ty = 42, level = 0, rotation = 1),
                Building(id = "compound-barricade-small", tx = 34, ty = 45, level = 0, rotation = 0),
                Building(id = "compound-barricade-small", tx = 18, ty = 29, level = 0, rotation = 0),
                Building(id = "compound-barricade-small", tx = 17, ty = 26, level = 0, rotation = 1),
                Building(id = "compound-barricade-small", tx = 14, ty = 27, level = 0, rotation = 2),
                Building(id = "deadend", tx = 31, ty = 51, level = 0, rotation = 0),
                Building(id = "deadend", tx = 32, ty = 38, level = 0, rotation = 0),
                Building(id = "deadend", tx = 25, ty = 60, level = 0, rotation = 3),
            )
        }
    }
}
