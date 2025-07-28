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
                Building(id = "B1", type = "bed", tx = 19, ty = 35, level = 2, rotation = 3),
                Building(id = "B2", type = "storage-ammunition", tx = 11, ty = 47, level = 5, rotation = 2),
                Building(id = "B3", type = "storage-cloth", tx = 12, ty = 38, level = 5, rotation = 0),
                Building(id = "B4", type = "storage-water", tx = 16, ty = 42, level = 5, rotation = 0),
                Building(id = "B5", type = "storage-metal", tx = 12, ty = 44, level = 5, rotation = 0),
                Building(id = "B6", type = "recycler", tx = 21, ty = 40, level = 9, rotation = 1),
                Building(id = "B7", type = "compound-barricade-small", tx = 17, ty = 62, level = 0, rotation = 3),
                Building(id = "B8", type = "compound-barricade-small", tx = 20, ty = 61, level = 0, rotation = 0),
                Building(id = "B9", type = "compound-barricade-small", tx = 15, ty = 59, level = 0, rotation = 2),
                Building(id = "B10", type = "compound-barricade-small", tx = 31, ty = 46, level = 0, rotation = 3),
                Building(id = "B11", type = "compound-barricade-small", tx = 33, ty = 42, level = 0, rotation = 1),
                Building(id = "B12", type = "compound-barricade-small", tx = 34, ty = 45, level = 0, rotation = 0),
                Building(id = "B13", type = "compound-barricade-small", tx = 18, ty = 29, level = 0, rotation = 0),
                Building(id = "B14", type = "compound-barricade-small", tx = 17, ty = 26, level = 0, rotation = 1),
                Building(id = "B15", type = "compound-barricade-small", tx = 14, ty = 27, level = 0, rotation = 2),
                Building(id = "B16", type = "deadend", tx = 31, ty = 51, level = 0, rotation = 0),
                Building(id = "B17", type = "deadend", tx = 32, ty = 38, level = 0, rotation = 0),
                Building(id = "B18", type = "deadend", tx = 25, ty = 60, level = 0, rotation = 3),
                Building(id = "B19", type = "rally", tx = 18, ty = 60, level = 0, rotation = 3),
                Building(id = "B20", type = "rally", tx = 32, ty = 44, level = 0, rotation = 0),
                Building(id = "B21", type = "rally", tx = 16, ty = 24, level = 0, rotation = 1),
            )
        }
    }
}
