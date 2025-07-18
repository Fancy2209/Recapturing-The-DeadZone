package dev.deadzone.core.model.game.data

import kotlinx.serialization.Serializable
import dev.deadzone.core.model.game.data.Item

@Serializable
data class SchematicItem(
    val item: Item,
    val type: String,
    val schem: String,
    val id: String = "",  // actually default a GUID.create()
    val new: Boolean = false,
    val storeId: String?
)
