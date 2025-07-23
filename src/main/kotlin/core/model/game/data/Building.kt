package dev.deadzone.core.model.game.data

import kotlinx.serialization.Serializable
import dev.deadzone.core.model.game.data.TimerData

@Serializable
data class Building(
    val id: String,
    val name: String? = null,
    val type: String = id,  // junk for JunkBuilding
    val level: Int,
    val rotation: Int,
    val tx: Int,
    val ty: Int,
    val destroyed: Boolean = false,
    val resourceValue: Double = 0.0,
    val upgrade: TimerData? = null,
    val repair: TimerData? = null
) {
    companion object {
        fun dummy(id: String = "", type: String = "", tx: Int = 10, ty: Int = 10): Building {
            return Building(
                id = id,
                type = id, // must be same with id. type lookup to XML is done in client-side
                level = 1,
                rotation = 0,
                tx = tx,
                ty = ty,
                destroyed = false,
                resourceValue = 0.0,
                upgrade = null,
                repair = null,
            )
        }

        fun bed(tx: Int = 50, ty: Int = 50): Building {
            return dummy(
                id = "bed",
                type = "bed",
                tx = tx,
                ty = ty
            )
        }
    }
}
