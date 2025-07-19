package dev.deadzone.api.message.db

import dev.deadzone.core.model.data.PlayerData
import dev.deadzone.core.model.game.data.Inventory
import dev.deadzone.core.model.network.RemotePlayerData
import dev.deadzone.core.utils.Converter
import kotlinx.serialization.Serializable

@Serializable
data class LoadObjectsOutput(
    val objects: List<BigDBObject> = listOf()
) {
    companion object {
        fun playerObjects(): LoadObjectsOutput {
            val data = PlayerData.dummy()
            val dbObject = Converter.toBigDBObject<PlayerData>(obj = data)

            return LoadObjectsOutput(objects = listOf(dbObject))
        }

        data class NeighborHistory(
            val map: Map<String, RemotePlayerData>?
        )

        fun neighborHistory(): LoadObjectsOutput {
            val data = NeighborHistory(mapOf())
            val dbObject = Converter.toBigDBObject<NeighborHistory>(obj = data)

            return LoadObjectsOutput(objects = listOf(dbObject))
        }

        fun inventory(): LoadObjectsOutput {
            val data = Inventory()
            val dbObject = Converter.toBigDBObject<Inventory>(obj = data)

            return LoadObjectsOutput(objects = listOf(dbObject))
        }
    }
}
