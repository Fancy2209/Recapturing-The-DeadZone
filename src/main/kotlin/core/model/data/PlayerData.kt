package dev.deadzone.core.model.data

import kotlinx.serialization.Serializable
import dev.deadzone.core.model.data.user.AbstractUser
import dev.deadzone.core.model.game.data.assignment.AssignmentCollection
import dev.deadzone.core.model.game.data.Attributes
import dev.deadzone.core.model.game.data.BatchRecycleJobCollection
import dev.deadzone.core.model.game.data.BuildingCollection
import dev.deadzone.core.model.game.data.CooldownCollection
import dev.deadzone.core.model.game.data.quests.DynamicQuest
import dev.deadzone.core.model.data.FlagSet
import dev.deadzone.core.model.game.data.GameResources
import dev.deadzone.core.model.game.data.quests.GlobalQuestData
import dev.deadzone.core.model.data.HighActivity
import dev.deadzone.core.model.game.data.bounty.InfectedBounty
import dev.deadzone.core.model.game.data.Inventory
import dev.deadzone.core.model.game.data.MissionCollection
import dev.deadzone.core.model.game.data.Survivor
import dev.deadzone.core.model.network.RemotePlayerData
import dev.deadzone.core.model.game.data.research.ResearchState
import dev.deadzone.core.model.game.data.skills.SkillCollection
import dev.deadzone.core.model.game.data.SurvivorCollection
import dev.deadzone.core.model.game.data.SurvivorLoadoutEntry
import dev.deadzone.core.model.game.data.TaskCollection

@Serializable
data class PlayerData(
    val key: String,
    val user: Map<String, AbstractUser> = mapOf(),
    val admin: Boolean,
    val allianceId: String,
    val allianceTag: String,
    val flags: FlagSet?,
    val upgrades: FlagSet?,
    val nickname: String,
    val playerSurvivor: String,
    val levelPts: UInt = 0u,
    val restXP: Int = 0,
    val oneTimePurchases: List<String> = listOf(),
    val neighbors: Map<String, RemotePlayerData>?,
    val friends: Map<String, RemotePlayerData>?,
    val neighborHistory: Map<String, RemotePlayerData>?,
    val research: ResearchState?,
    val skills: SkillCollection?,
    val resources: GameResources,
    val survivors: SurvivorCollection,
    val playerAttributes: Attributes,
    val buildings: BuildingCollection,
    val rally: Map<String, List<String>>?,  // key building id, value list of survivor ids
    val tasks: TaskCollection,
    val missions: MissionCollection?,
    val assignments: AssignmentCollection?,
    val inventory: Inventory?,
    val effects: Map<String, String>?,
    val globalEffects: Map<String, String>?,
    val cooldowns: CooldownCollection?,
    val batchRecycles: BatchRecycleJobCollection?,
    val offenceLoadout: Map<String, SurvivorLoadoutEntry>?,
    val defenceLoadout: Map<String, SurvivorLoadoutEntry>?,
    val quests: ByteArray?,  // parsed by booleanArrayFromByteArray
    val questsCollected: ByteArray?,  // parsed by booleanArrayFromByteArray
    val achievements: ByteArray?,  // parsed by booleanArrayFromByteArray
    val dailyQuest: DynamicQuest?,
    val questsTracked: String?,  // each quest separated with |
    val gQuestsV2: GlobalQuestData?,
    val bountyCap: Int,
    val lastogout: Long?,
    val dzBounty: InfectedBounty?,
    val nextDZBountyIssue: Long,
    val highActivity: HighActivity?,  // unknown which class is this so we make custom class
    val invsize: Int
) {
    companion object {
        fun dummy(): PlayerData {
            return PlayerData(
                key = "exampleKey",
                admin = true,
                allianceId = "",
                allianceTag = "",
                flags = null,
                upgrades = null,
                nickname = "dzplayer",
                playerSurvivor = "survivor-player",
                neighbors = null,
                friends = null,
                neighborHistory = null,
                research = null,
                skills = null,
                resources = GameResources(cash = 100000, wood = 100, 100, 100, 100, 100, 100),
                survivors = SurvivorCollection(Survivor()),
                playerAttributes = TODO(),
                buildings = TODO(),
                rally = TODO(),
                tasks = TODO(),
                missions = TODO(),
                assignments = TODO(),
                inventory = TODO(),
                effects = TODO(),
                globalEffects = TODO(),
                cooldowns = TODO(),
                batchRecycles = TODO(),
                offenceLoadout = TODO(),
                defenceLoadout = TODO(),
                quests = TODO(),
                questsCollected = TODO(),
                achievements = TODO(),
                dailyQuest = TODO(),
                questsTracked = TODO(),
                gQuestsV2 = TODO(),
                bountyCap = TODO(),
                lastogout = TODO(),
                dzBounty = TODO(),
                nextDZBountyIssue = TODO(),
                highActivity = TODO(),
                invsize = TODO()
            )
        }
    }
}
