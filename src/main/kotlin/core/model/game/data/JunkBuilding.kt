package dev.deadzone.core.model.game.data

import kotlinx.serialization.Serializable
import dev.deadzone.core.model.game.data.Building
import dev.deadzone.core.model.game.data.Item
import kotlinx.serialization.SerialName
import java.util.UUID

@Serializable
data class JunkBuilding(
    // from Building
    val id: String = UUID.randomUUID().toString(),
    val name: String? = null,
    val type: String,
    val level: Int = 0,
    val rotation: Int = 0,
    val tx: Int = 0,
    val ty: Int = 0,
    val destroyed: Boolean = false,
    val resourceValue: Double = 0.0,
    val upgrade: TimerData? = null,
    val repair: TimerData? = null,

    // JunkBuilding-specific fields
    val items: List<Item> = emptyList(),
    val pos: String,
    val rot: String
) : BuildingLike()
