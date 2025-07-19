package dev.deadzone.core.model.game.data

import kotlinx.serialization.Serializable
import dev.deadzone.core.model.game.data.TimerData

@Serializable
data class Building(
    val id: String,
    val name: String? = null,
    val type: String,  // junk for JunkBuilding
    val level: Int,
    val rotation: Int,
    val tx: Int,
    val ty: Int,
    val destroyed: Boolean,
    val resourceValue: Double = 0.0,
    val upgrade: TimerData?,
    val repair: TimerData?
) {
    companion object {
        fun dummy(id: String = "", type: String = ""): Building {
            return Building(
                id = id,
                type = type,
                level = 1,
                rotation = 0,
                tx = 10,
                ty = 10,
                destroyed = false,
                resourceValue = 0.0,
                upgrade = null,
                repair = null,
            )
        }
    }
}
