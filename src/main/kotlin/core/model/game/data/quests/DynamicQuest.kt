package dev.deadzone.core.model.game.data.quests

import dev.deadzone.core.model.game.data.MoraleConstants_Constants
import kotlinx.serialization.Serializable
import dev.deadzone.core.model.game.data.quests.DynamicQuestGoal
import dev.deadzone.core.model.game.data.quests.DynamicQuestPenalty
import dev.deadzone.core.model.game.data.quests.DynamicQuestReward
import dev.deadzone.core.model.game.data.quests.Quest
import io.ktor.util.date.getTimeMillis
import java.io.ByteArrayOutputStream
import java.io.DataOutputStream
import java.nio.ByteBuffer
import java.nio.ByteOrder

@Serializable
data class DynamicQuest(
    val raw: ByteArray,  // see DynamicQuest.as for detail of structure
    val quest: Quest,  // inherited
    val questType: Int,
    val accepted: Boolean,
    val goals: List<DynamicQuestGoal> = listOf(),
    val rewards: List<DynamicQuestReward> = listOf(),
    val failurePenalties: List<DynamicQuestPenalty> = listOf()
) {
    companion object {
        fun dummy(): ByteArray {
            val buffer = ByteBuffer.allocate(1024).order(ByteOrder.LITTLE_ENDIAN)

            buffer.putShort(2)

            // Quest Type
            buffer.putShort(1)

            // Quest ID
            val questId = "comfortQuest"
            buffer.putUTF(questId)

            // Booleans
            buffer.put(0) // accepted = true
            buffer.put(0) // complete = false
            buffer.put(0) // collected = false
            buffer.put(0) // failed = false

            // End time (Date)
            buffer.putDouble(getTimeMillis().toDouble())

            // Goals length
            buffer.putShort(1) // 1 goal

            // Create sub-bytearray for goal
            val goalData = ByteArrayOutputStream()
            val goal = DataOutputStream(goalData)
            // write length before each string, but this is done via writeUTF
            goal.writeUTF("statInc")         // goal type
            goal.writeUTF("kills")           // stat
            goal.writeInt(10)                // goal count

            val goalBytes = goalData.toByteArray()
            buffer.putShort(goalBytes.size.toShort())
            buffer.put(goalBytes)

            // ---------- Rewards ----------
            buffer.putShort(1) // 1 reward

            val rewardData = ByteArrayOutputStream()
            val reward = DataOutputStream(rewardData)
            reward.writeShort(0)             // type = 0 (xp)
            reward.writeInt(500)             // XP amount

            val rewardBytes = rewardData.toByteArray()
            buffer.putShort(rewardBytes.size.toShort())
            buffer.put(rewardBytes)

            // ---------- Failure Penalties ----------
            buffer.putShort(1) // 1 penalty

            val penaltyData = ByteArrayOutputStream()
            val penalty = DataOutputStream(penaltyData)
            penalty.writeShort(2)                 // type = 2 (morale)
            penalty.writeUTF("food")             // moraleType
            penalty.writeDouble(5.0)             // value

            val penaltyBytes = penaltyData.toByteArray()
            buffer.putShort(penaltyBytes.size.toShort())
            buffer.put(penaltyBytes)

            // ---------- Version-specific int ----------
            buffer.putInt(12345678)

            return buffer.array().sliceArray(0 until buffer.position())
        }
    }
}

private fun ByteBuffer.putUTF(questId: String) {
    val utfBytes = questId.toByteArray(Charsets.UTF_8)
    require(utfBytes.size <= 65535) { "String too long for UTF format." }
    this.putShort(utfBytes.size.toShort()) // Length prefix
    this.put(utfBytes)
}
