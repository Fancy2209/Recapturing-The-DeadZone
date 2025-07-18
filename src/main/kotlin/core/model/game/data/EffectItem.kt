package dev.deadzone.core.model.game.data

import kotlinx.serialization.Serializable
import dev.deadzone.core.model.game.data.Item

@Serializable
data class EffectItem(
    val item: Item,  // inherited
    val effect: ByteArray,  // see effect.as for bytearray specification
)
