package dev.deadzone.core.model.game.data

import kotlinx.serialization.Serializable
import dev.deadzone.core.model.game.data.EffectItem

@Serializable
data class MiscEffectItem(
    val effectItem: EffectItem
)
