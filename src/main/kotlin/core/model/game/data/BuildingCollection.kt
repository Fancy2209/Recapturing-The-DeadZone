package dev.deadzone.core.model.game.data

import kotlinx.serialization.Serializable
import java.util.UUID

@Serializable
data class BuildingCollection(
    val list: List<BuildingLike> = listOf()
) {
    companion object {
        fun starterBase(): List<BuildingLike> {
            return listOf(
                JunkBuilding.create(id = UUID.randomUUID().toString(), type = "junk1", tx = 10, ty = 20, rotation = 0),
                JunkBuilding.create(id = UUID.randomUUID().toString(), type = "junk-tutorial", tx = 12, ty = 20, rotation = 1),
                JunkBuilding.create(id = UUID.randomUUID().toString(), type = "junk-pile-corner", tx = 14, ty = 22, rotation = 0),
                JunkBuilding.create(id = UUID.randomUUID().toString(), type = "junk-cloth", tx = 16, ty = 23, rotation = 2),
                JunkBuilding.create(id = UUID.randomUUID().toString(), type = "junk-drums", tx = 18, ty = 24, rotation = 3),
                JunkBuilding.create(id = UUID.randomUUID().toString(), type = "junk-pallets", tx = 20, ty = 25, rotation = 1),
                JunkBuilding.create(id = UUID.randomUUID().toString(), type = "junk-pile-mid", tx = 22, ty = 27, rotation = 0),
                JunkBuilding.create(id = UUID.randomUUID().toString(), type = "junk-pile-small", tx = 24, ty = 29, rotation = 2),
                JunkBuilding.create(id = UUID.randomUUID().toString(), type = "junk-pile-huge", tx = 26, ty = 31, rotation = 0),
                JunkBuilding.create(id = UUID.randomUUID().toString(), type = "junk-pile-huge-2", tx = 28, ty = 33, rotation = 1),
                JunkBuilding.create(id = UUID.randomUUID().toString(), type = "junk-pile-car", tx = 30, ty = 35, rotation = 3),
                JunkBuilding.create(id = UUID.randomUUID().toString(), type = "junk-machinery-small", tx = 32, ty = 37, rotation = 0),
                JunkBuilding.create(id = UUID.randomUUID().toString(), type = "junk-machinery-large", tx = 34, ty = 40, rotation = 2),
                Building(id = UUID.randomUUID().toString(), type = "car", tx = 36, ty = 42, rotation = 1)
            )
        }

        fun simpleBase(): List<BuildingLike> {
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

        fun goodBase(): List<BuildingLike> {
            return listOf(
                // inside
                Building(id = "B1", type = "bed", tx = 9, ty = 46, level = 4, rotation = 0),
                Building(id = "B2", type = "bed", tx = 9, ty = 43, level = 4, rotation = 0),
                Building(id = "B3", type = "bed", tx = 9, ty = 40, level = 4, rotation = 0),
                Building(id = "B4", type = "shower", tx = 15, ty = 50, level = 4, rotation = 0),
                Building(id = "B5", type = "shower", tx = 18, ty = 50, level = 4, rotation = 0),
                Building(id = "B6", type = "storage-ammunition", tx = 15, ty = 42, level = 5, rotation = 0),
                Building(id = "B7", type = "storage-ammunition", tx = 18, ty = 42, level = 5, rotation = 0),
                Building(id = "B8", type = "storage-cloth", tx = 7, ty = 34, level = 5, rotation = 3),
                Building(id = "B9", type = "storage-cloth", tx = 24, ty = 34, level = 5, rotation = 3),
                Building(id = "B10", type = "storage-water", tx = 8, ty = 55, level = 5, rotation = 0),
                Building(id = "B11", type = "storage-water", tx = 8, ty = 52, level = 5, rotation = 0),
                Building(id = "B12", type = "storage-metal", tx = 27, ty = 55, level = 5, rotation = 0),
                Building(id = "B13", type = "storage-metal", tx = 24, ty = 55, level = 5, rotation = 0),
                Building(id = "B14", type = "recycler", tx = 14, ty = 34, level = 9, rotation = 0),
                Building(id = "B15", type = "workbench", tx = 14, ty = 45, level = 4, rotation = 3),
                Building(id = "B16", type = "bench-engineering", tx = 19, ty = 34, level = 4, rotation = 3),
                Building(id = "B17", type = "bench-weapon", tx = 19, ty = 44, level = 4, rotation = 0),

                // right side
                Building(id = "B18", type = "barricadeSmall", tx = 13, ty = 28, level = 4, rotation = 2),
                Building(id = "B19", type = "barricadeSmall", tx = 15, ty = 27, level = 4, rotation = 1),
                Building(id = "B20", type = "barricadeSmall", tx = 18, ty = 27, level = 4, rotation = 1),
                Building(id = "B21", type = "barricadeSmall", tx = 18, ty = 30, level = 4, rotation = 0),
                Building(id = "B22", type = "door", tx = 17, ty = 57, level = 4, rotation = 3),
                Building(id = "B23", type = "rally", tx = 16, ty = 24, level = 0, rotation = 1),

                // outer right side
                Building(id = "B24", type = "defence-wire", tx = 26, ty = 15, level = 3, rotation = 1),
                Building(id = "B25", type = "defence-wire", tx = 32, ty = 15, level = 3, rotation = 1),
                Building(id = "B26", type = "defence-wire", tx = 38, ty = 15, level = 3, rotation = 1),
                Building(id = "B27", type = "defence-wire", tx = 38, ty = 21, level = 3, rotation = 0),
                Building(id = "B28", type = "defence-wire", tx = 38, ty = 27, level = 3, rotation = 0),
                Building(id = "B29", type = "windmill", tx = 33, ty = 20, level = 4, rotation = 1),
                Building(id = "B30", type = "trap-halloween-decoy", tx = 10, ty = 14, level = 0, rotation = 1),
                Building(id = "B31", type = "trap-halloween-decoy", tx = 8, ty = 14, level = 0, rotation = 1),

                // front side
                Building(id = "B32", type = "barricadeSmall", tx = 32, ty = 35, level = 4, rotation = 1),
                Building(id = "B33", type = "barricadeSmall", tx = 35, ty = 35, level = 4, rotation = 1),
                Building(id = "B34", type = "barricadeLarge", tx = 30, ty = 57, level = 4, rotation = 3),
                Building(id = "B35", type = "barricadeLarge", tx = 36, ty = 57, level = 4, rotation = 3),
                Building(id = "B36", type = "barricadeLarge", tx = 41, ty = 56, level = 4, rotation = 0),
                Building(id = "B37", type = "barricadeLarge", tx = 41, ty = 47, level = 4, rotation = 0),
                Building(id = "B38", type = "barricadeLarge", tx = 41, ty = 41, level = 4, rotation = 0),
                Building(id = "B39", type = "barricadeLarge", tx = 41, ty = 35, level = 4, rotation = 1),
                Building(id = "B40", type = "watchtower", tx = 31, ty = 53, level = 3, rotation = 3),
                Building(id = "B41", type = "watchtower", tx = 33, ty = 39, level = 3, rotation = 0),
                Building(id = "B42", type = "gate", tx = 41, ty = 50, level = 4, rotation = 0),
                Building(id = "B43", type = "deadend", tx = 37, ty = 50, level = 0, rotation = 0),
                Building(id = "B44", type = "deadend", tx = 37, ty = 38, level = 0, rotation = 0),
                Building(id = "B45", type = "rally", tx = 32, ty = 44, level = 0, rotation = 0),

                // left side
                Building(id = "B46", type = "barricadeLarge", tx = 14, ty = 58, level = 4, rotation = 2),
                Building(id = "B47", type = "barricadeLarge", tx = 15, ty = 63, level = 4, rotation = 3),
                Building(id = "B48", type = "barricadeLarge", tx = 21, ty = 63, level = 4, rotation = 0),
                Building(id = "B49", type = "construction-yard", tx = 28, ty = 59, level = 4, rotation = 1),
                Building(id = "B50", type = "door", tx = 16, ty = 31, level = 4, rotation = 1),
                Building(id = "B51", type = "deadend", tx = 18, ty = 67, level = 0, rotation = 3),
                Building(id = "B52", type = "rally", tx = 17, ty = 59, level = 0, rotation = 3),
            )
        }
    }
}
