package dev.deadzone.core.model.game.data

import kotlinx.serialization.Serializable
import dev.deadzone.core.model.game.data.Building
import dev.deadzone.core.model.game.data.Item
import kotlinx.serialization.SerialName
import java.util.UUID

@Serializable
data class JunkBuilding(
    // from Building
    val id: String,
    val name: String? = null,
    val type: String,
    val level: Int = 0,
    val rotation: Int,
    val tx: Int,
    val ty: Int,
    val destroyed: Boolean = false,
    val resourceValue: Double = 0.0,
    val upgrade: TimerData? = null,
    val repair: TimerData? = null,

    // JunkBuilding-specific fields
    val items: List<Item> = emptyList(),
    val pos: String,
    val rot: String
) : BuildingLike() {
    companion object {
        fun create(
            id: String = UUID.randomUUID().toString(),
            type: String,
            tx: Int,
            ty: Int,
            rotation: Int = 0,
            level: Int = 0,
            items: List<Item> = emptyList()
        ): JunkBuilding {
            return JunkBuilding(
                id = id,
                type = type,
                tx = tx,
                ty = ty,
                rotation = rotation,
                level = level,
                items = items,
                pos = "$tx,$ty,0",
                rot = "0.0,0.0,${(rotation % 4) * 90f}"
            )
        }
    }
}
