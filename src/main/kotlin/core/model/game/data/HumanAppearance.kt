package dev.deadzone.core.model.game.data

import kotlinx.serialization.Serializable
import dev.deadzone.core.model.game.data.AttireData

@Serializable
data class HumanAppearance(
    val forceHair: Boolean,
    val hideGear: Boolean,
    val hairColor: String = "black",
    val skinColor: AttireData?,
    val hair: AttireData?,
    val facialhair: AttireData?,
    val clothing_upper: AttireData?,
    val clothing_lower: AttireData?,
    val accessories: List<AttireData>?
)
