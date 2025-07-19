package dev.deadzone.core.model.data

import kotlinx.serialization.Serializable

@Serializable
@JvmInline
value class PlayerFlags(val value: UInt)

object PlayerFlags_Constants {
    val NicknameVerified = PlayerFlags(0u)
    val RefreshNeighbors = PlayerFlags(1u)
    val TutorialComplete = PlayerFlags(2u)
    val InjurySustained = PlayerFlags(3u)
    val InjuryHelpComplete = PlayerFlags(4u)
    val AutoProtectionApplied = PlayerFlags(5u)
    val TutorialCrateFound = PlayerFlags(6u)
    val TutorialCrateUnlocked = PlayerFlags(7u)
    val TutorialSchematicFound = PlayerFlags(8u)
    val TutorialEffectFound = PlayerFlags(9u)
    val TutorialPvPPractice = PlayerFlags(10u)
}
