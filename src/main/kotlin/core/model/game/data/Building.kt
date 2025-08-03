package dev.deadzone.core.model.game.data

import kotlinx.serialization.Serializable
import dev.deadzone.core.model.game.data.TimerData

@Serializable
data class Building(
    val id: String,    // building's unique ID
    val name: String? = null,
    val type: String,  // building's ID in buildings.xml, not to be confused with type in XML
    val level: Int,
    val rotation: Int,
    val tx: Int,
    val ty: Int,
    val destroyed: Boolean = false,
    val resourceValue: Double = 0.0,
    val upgrade: TimerData? = null,
    val repair: TimerData? = null
)
