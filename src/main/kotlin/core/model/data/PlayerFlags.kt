package dev.deadzone.core.model.data

import kotlin.experimental.or

object PlayerFlags {
    // if nicknameVerified = true, then skip character creation

    fun create(
        nicknameVerified: Boolean = false, refreshNeighbors: Boolean = false,
        tutorialComplete: Boolean = false, injurySustained: Boolean = false,
        injuryHelpComplete: Boolean = false, autoProtectionApplied: Boolean = false,
        tutorialCrateFound: Boolean = false, tutorialCrateUnlocked: Boolean = false,
        tutorialSchematicFound: Boolean = false, tutorialEffectFound: Boolean = false,
        tutorialPvPPractice: Boolean = false,
    ): ByteArray {
        val flags = listOf(
            nicknameVerified, refreshNeighbors, tutorialComplete,
            injurySustained, injuryHelpComplete, autoProtectionApplied,
            tutorialCrateFound, tutorialCrateUnlocked, tutorialSchematicFound,
            tutorialEffectFound, tutorialPvPPractice,
        )
        return flags.toByteArray()
    }

    fun skipTutorial(
        nicknameVerified: Boolean = true, refreshNeighbors: Boolean = false,
        tutorialComplete: Boolean = true, injurySustained: Boolean = true,
        injuryHelpComplete: Boolean = true, autoProtectionApplied: Boolean = true,
        tutorialCrateFound: Boolean = true, tutorialCrateUnlocked: Boolean = true,
        tutorialSchematicFound: Boolean = true, tutorialEffectFound: Boolean = true,
        tutorialPvPPractice: Boolean = true,
    ): ByteArray {
        val flags = listOf(
            nicknameVerified, refreshNeighbors, tutorialComplete,
            injurySustained, injuryHelpComplete, autoProtectionApplied,
            tutorialCrateFound, tutorialCrateUnlocked, tutorialSchematicFound,
            tutorialEffectFound, tutorialPvPPractice,
        )

        return flags.toByteArray()
    }
}

fun List<Boolean>.toByteArray(): ByteArray {
    val bytes = ByteArray(this.size)

    for (i in this.indices) {
        if (this[i]) {
            val byteIndex = i / 8
            val bitIndex = i % 8
            bytes[byteIndex] = bytes[byteIndex] or (1 shl bitIndex).toByte()
        }
    }

    return bytes
}

object PlayerFlags_Constants {
    val NicknameVerified = 0u
    val RefreshNeighbors = 1u
    val TutorialComplete = 2u
    val InjurySustained = 3u
    val InjuryHelpComplete = 4u
    val AutoProtectionApplied = 5u
    val TutorialCrateFound = 6u
    val TutorialCrateUnlocked = 7u
    val TutorialSchematicFound = 8u
    val TutorialEffectFound = 9u
    val TutorialPvPPractice = 10u
}
