package dev.deadzone.core.model.game.data.quests

import kotlinx.serialization.Serializable
import dev.deadzone.core.model.game.data.quests.DynamicQuestGoal
import dev.deadzone.core.model.game.data.quests.DynamicQuestPenalty
import dev.deadzone.core.model.game.data.quests.DynamicQuestReward
import dev.deadzone.core.model.game.data.quests.Quest

@Serializable
data class DynamicQuest(
    val raw: ByteArray,  // see DynamicQuest.as for detail of structure
    val quest: Quest,  // inherited
    val questType: Int,
    val accepted: Boolean,
    val goals: List<DynamicQuestGoal> = listOf(),
    val rewards: List<DynamicQuestReward> = listOf(),
    val failurePenalties: List<DynamicQuestPenalty> = listOf()
)
