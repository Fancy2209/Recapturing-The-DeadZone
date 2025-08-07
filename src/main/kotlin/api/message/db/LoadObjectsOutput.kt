package dev.deadzone.api.message.db

import dev.deadzone.core.model.data.PlayerData
import dev.deadzone.core.model.game.data.Inventory
import dev.deadzone.core.model.network.RemotePlayerData
import dev.deadzone.utils.Converter
import kotlinx.serialization.Serializable

@Serializable
data class LoadObjectsOutput(
    val objects: List<BigDBObject> = listOf()
) {
    companion object {
        fun playerObjects(): BigDBObject {
            val data = PlayerData.admin()
            return Converter.toBigDBObject<PlayerData>(obj = data)
        }

        data class NeighborHistory(
            val map: Map<String, RemotePlayerData>? = emptyMap()
        )

        fun neighborHistory(): BigDBObject {
            val data = NeighborHistory(emptyMap())
            return Converter.toBigDBObject<NeighborHistory>(obj = data)
        }

        fun inventory(): BigDBObject {
            val data = Inventory.dummy()
            return Converter.toBigDBObject<Inventory>(obj = data)
        }
    }
}
