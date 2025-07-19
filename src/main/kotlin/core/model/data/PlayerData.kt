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
import dev.deadzone.core.model.game.data.BatchRecycleJob
import dev.deadzone.core.model.game.data.Building
import dev.deadzone.core.model.game.data.Gender_Constants
import dev.deadzone.core.model.game.data.bounty.InfectedBounty
import dev.deadzone.core.model.game.data.Inventory
import dev.deadzone.core.model.game.data.MissionCollection
import dev.deadzone.core.model.game.data.MissionData
import dev.deadzone.core.model.game.data.Survivor
import dev.deadzone.core.model.game.data.SurvivorClassConstants_Constants
import dev.deadzone.core.model.network.RemotePlayerData
import dev.deadzone.core.model.game.data.research.ResearchState
import dev.deadzone.core.model.game.data.skills.SkillCollection
import dev.deadzone.core.model.game.data.SurvivorCollection
import dev.deadzone.core.model.game.data.SurvivorLoadoutEntry
import dev.deadzone.core.model.game.data.Task
import dev.deadzone.core.model.game.data.TaskCollection
import dev.deadzone.core.model.game.data.assignment.AssignmentData
import dev.deadzone.core.model.game.data.quests.GQDataObj
import dev.deadzone.core.model.game.data.skills.SkillState

@Serializable
data class PlayerData(
    val key: String,
    val user: Map<String, AbstractUser> = mapOf(),
    val admin: Boolean,
    val allianceId: String,
    val allianceTag: String,
    val flags: ByteArray?,         // deserialized to flagset
    val upgrades: ByteArray?,      // deserialized to flagset
    val nickname: String,
    val playerSurvivor: String,
    val levelPts: UInt = 0u,
    val restXP: Int = 0,
    val oneTimePurchases: List<String> = listOf(),
    val neighbors: Map<String, RemotePlayerData>?,
    val friends: Map<String, RemotePlayerData>?,
    val neighborHistory: Map<String, RemotePlayerData>?,
    val research: ResearchState?,
    val skills: Map<String, SkillState>?,
    val resources: GameResources,
    val survivors: List<Survivor>,
    val playerAttributes: Attributes,
    val buildings: List<Building>,
    val rally: Map<String, List<String>>?,  // key building id, value list of survivor ids
    val tasks: List<Task>,
    val missions: List<MissionData>?,
    val assignments: List<AssignmentData>?,
    val inventory: Inventory?,
    val effects: Map<String, String>?,
    val globalEffects: Map<String, String>?,
    val cooldowns: Map<String, ByteArray>?,
    val batchRecycles: List<BatchRecycleJob>?,
    val offenceLoadout: Map<String, SurvivorLoadoutEntry>?,
    val defenceLoadout: Map<String, SurvivorLoadoutEntry>?,
    val quests: ByteArray?,  // parsed by booleanArrayFromByteArray
    val questsCollected: ByteArray?,  // parsed by booleanArrayFromByteArray
    val achievements: ByteArray?,  // parsed by booleanArrayFromByteArray
    val dailyQuest: DynamicQuest?,
    val questsTracked: String?,  // each quest separated with |
    val gQuestsV2: Map<String, GQDataObj>?,
    val bountyCap: Int,
    val lastLogout: Long?,
    val dzBounty: InfectedBounty?,
    val nextDZBountyIssue: Long,
    val highActivity: HighActivity?,  // unknown which class is this so we make custom class
    val invsize: Int,
    val zombieAttack: Boolean,
    val zombieAttackLogins: Int,
    val offersEnabled: Boolean,
    val prevLogin: PrevLogin?,
    val lastLogin: Long?,
    val notifications: List<Notification?>?,

    ) {
    companion object {
        fun dummy(): PlayerData {
            val srvId = "survivor-player"

            return PlayerData(
                key = "exampleKey",
                admin = true,
                allianceId = "",
                allianceTag = "",
                flags = FlagSet.mockFlagSetByteArray(),
                upgrades = FlagSet.mockFlagSetByteArray(),
                nickname = "dzplayer",
                playerSurvivor = srvId,
                neighbors = null,
                friends = null,
                neighborHistory = null,
                research = null,
                skills = null,
                resources = GameResources(
                    cash = 100000,
                    wood = 100,
                    metal = 100,
                    cloth = 100,
                    water = 100,
                    food = 100,
                    ammunition = 100
                ),
                survivors = SurvivorCollection.dummy(
                    srvId, classId = SurvivorClassConstants_Constants.FIGHTER
                ),
                playerAttributes = Attributes.dummy(),
                buildings = BuildingCollection().list,
                rally = null,
                tasks = TaskCollection().list,
                missions = null,
                assignments = null,
                inventory = null,
                effects = null,
                globalEffects = null,
                cooldowns = null,
                batchRecycles = null,
                offenceLoadout = null,
                defenceLoadout = null,
                quests = null,
                questsCollected = null,
                achievements = null,
                dailyQuest = null,
                questsTracked = null,
                gQuestsV2 = null,
                bountyCap = 0,
                lastLogout = null,
                dzBounty = null,
                nextDZBountyIssue = 1_000_000_000,
                highActivity = null,
                invsize = 8,
                zombieAttack = false,
                zombieAttackLogins = 0,
                offersEnabled = false,
                prevLogin = null,
                lastLogin = null,
                notifications = listOf(),
            )
        }
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as PlayerData

        if (admin != other.admin) return false
        if (restXP != other.restXP) return false
        if (bountyCap != other.bountyCap) return false
        if (lastLogout != other.lastLogout) return false
        if (nextDZBountyIssue != other.nextDZBountyIssue) return false
        if (invsize != other.invsize) return false
        if (zombieAttack != other.zombieAttack) return false
        if (zombieAttackLogins != other.zombieAttackLogins) return false
        if (offersEnabled != other.offersEnabled) return false
        if (lastLogin != other.lastLogin) return false
        if (key != other.key) return false
        if (user != other.user) return false
        if (allianceId != other.allianceId) return false
        if (allianceTag != other.allianceTag) return false
        if (!flags.contentEquals(other.flags)) return false
        if (!upgrades.contentEquals(other.upgrades)) return false
        if (nickname != other.nickname) return false
        if (playerSurvivor != other.playerSurvivor) return false
        if (levelPts != other.levelPts) return false
        if (oneTimePurchases != other.oneTimePurchases) return false
        if (neighbors != other.neighbors) return false
        if (friends != other.friends) return false
        if (neighborHistory != other.neighborHistory) return false
        if (research != other.research) return false
        if (skills != other.skills) return false
        if (resources != other.resources) return false
        if (survivors != other.survivors) return false
        if (playerAttributes != other.playerAttributes) return false
        if (buildings != other.buildings) return false
        if (rally != other.rally) return false
        if (tasks != other.tasks) return false
        if (missions != other.missions) return false
        if (assignments != other.assignments) return false
        if (inventory != other.inventory) return false
        if (effects != other.effects) return false
        if (globalEffects != other.globalEffects) return false
        if (cooldowns != other.cooldowns) return false
        if (batchRecycles != other.batchRecycles) return false
        if (offenceLoadout != other.offenceLoadout) return false
        if (defenceLoadout != other.defenceLoadout) return false
        if (!quests.contentEquals(other.quests)) return false
        if (!questsCollected.contentEquals(other.questsCollected)) return false
        if (!achievements.contentEquals(other.achievements)) return false
        if (dailyQuest != other.dailyQuest) return false
        if (questsTracked != other.questsTracked) return false
        if (gQuestsV2 != other.gQuestsV2) return false
        if (dzBounty != other.dzBounty) return false
        if (highActivity != other.highActivity) return false
        if (prevLogin != other.prevLogin) return false
        if (notifications != other.notifications) return false

        return true
    }

    override fun hashCode(): Int {
        var result = admin.hashCode()
        result = 31 * result + restXP
        result = 31 * result + bountyCap
        result = 31 * result + (lastLogout?.hashCode() ?: 0)
        result = 31 * result + nextDZBountyIssue.hashCode()
        result = 31 * result + invsize
        result = 31 * result + zombieAttack.hashCode()
        result = 31 * result + zombieAttackLogins
        result = 31 * result + offersEnabled.hashCode()
        result = 31 * result + (lastLogin?.hashCode() ?: 0)
        result = 31 * result + key.hashCode()
        result = 31 * result + user.hashCode()
        result = 31 * result + allianceId.hashCode()
        result = 31 * result + allianceTag.hashCode()
        result = 31 * result + (flags?.contentHashCode() ?: 0)
        result = 31 * result + (upgrades?.contentHashCode() ?: 0)
        result = 31 * result + nickname.hashCode()
        result = 31 * result + playerSurvivor.hashCode()
        result = 31 * result + levelPts.hashCode()
        result = 31 * result + oneTimePurchases.hashCode()
        result = 31 * result + (neighbors?.hashCode() ?: 0)
        result = 31 * result + (friends?.hashCode() ?: 0)
        result = 31 * result + (neighborHistory?.hashCode() ?: 0)
        result = 31 * result + (research?.hashCode() ?: 0)
        result = 31 * result + (skills?.hashCode() ?: 0)
        result = 31 * result + resources.hashCode()
        result = 31 * result + survivors.hashCode()
        result = 31 * result + playerAttributes.hashCode()
        result = 31 * result + buildings.hashCode()
        result = 31 * result + (rally?.hashCode() ?: 0)
        result = 31 * result + tasks.hashCode()
        result = 31 * result + (missions?.hashCode() ?: 0)
        result = 31 * result + (assignments?.hashCode() ?: 0)
        result = 31 * result + (inventory?.hashCode() ?: 0)
        result = 31 * result + (effects?.hashCode() ?: 0)
        result = 31 * result + (globalEffects?.hashCode() ?: 0)
        result = 31 * result + (cooldowns?.hashCode() ?: 0)
        result = 31 * result + (batchRecycles?.hashCode() ?: 0)
        result = 31 * result + (offenceLoadout?.hashCode() ?: 0)
        result = 31 * result + (defenceLoadout?.hashCode() ?: 0)
        result = 31 * result + (quests?.contentHashCode() ?: 0)
        result = 31 * result + (questsCollected?.contentHashCode() ?: 0)
        result = 31 * result + (achievements?.contentHashCode() ?: 0)
        result = 31 * result + (dailyQuest?.hashCode() ?: 0)
        result = 31 * result + (questsTracked?.hashCode() ?: 0)
        result = 31 * result + (gQuestsV2?.hashCode() ?: 0)
        result = 31 * result + (dzBounty?.hashCode() ?: 0)
        result = 31 * result + (highActivity?.hashCode() ?: 0)
        result = 31 * result + (prevLogin?.hashCode() ?: 0)
        result = 31 * result + (notifications?.hashCode() ?: 0)
        return result
    }
}
