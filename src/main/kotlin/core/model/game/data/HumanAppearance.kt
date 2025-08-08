package dev.deadzone.core.model.game.data

import kotlinx.serialization.Serializable
import dev.deadzone.core.model.game.data.AttireData

// HumanAppearance is the base class of SurvivorAppearance
// the game can't take both, because SurvivorAppearance doesn't have deserialize method
// and it delegates deserialize to HumanAppearance itself
// The difference between HumanAppearance and SurvivorAppearance is within clothing_upper/lower
// and accessories field
@Serializable
data class HumanAppearance(
    val forceHair: Boolean = false,
    val hideGear: Boolean = false,
    val hairColor: String = "black",
    val skinColor: String? = null,
    val hair: String? = null,
    val facialHair: String? = null,
    val clothing_upper: String? = null,
    val clothing_lower: String? = null,
    val accessories: List<String>? = null
)

//@Serializable
//data class HumanAppearance(
//    val forceHair: Boolean = false,
//    val hideGear: Boolean = false,
//    val hairColor: String = "black",
//    val skinColor: AttireData? = null,
//    val hair: AttireData? = null,
//    val facialhair: AttireData? = null,
//    val clothing_upper: AttireData? = null,
//    val clothing_lower: AttireData? = null,
//    val accessories: List<AttireData>? = null
//)
