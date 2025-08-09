package dev.deadzone.core.data

import com.mongodb.client.model.Filters
import com.mongodb.client.model.Indexes
import com.mongodb.client.model.Updates
import com.mongodb.kotlin.client.coroutine.MongoCollection
import com.mongodb.kotlin.client.coroutine.MongoDatabase
import com.toxicbakery.bcrypt.Bcrypt
import dev.deadzone.core.auth.model.ServerMetadata
import dev.deadzone.data.collection.PlayerAccount
import dev.deadzone.core.auth.model.UserProfile
import dev.deadzone.core.model.data.PlayerFlags
import dev.deadzone.core.model.game.data.HumanAppearance
import dev.deadzone.data.collection.Inventory
import dev.deadzone.data.collection.NeighborHistory
import dev.deadzone.user.model.PlayerMetadata
import dev.deadzone.data.collection.PlayerObjects
import dev.deadzone.data.db.BigDB
import dev.deadzone.module.Logger
import io.ktor.util.date.getTimeMillis
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import java.util.*
import kotlin.io.encoding.Base64

class BigDBMongoImpl(db: MongoDatabase, private val adminEnabled: Boolean) : BigDB {
    private val plyCollection = db.getCollection<PlayerAccount>("playeraccount")
    private val objCollection = db.getCollection<PlayerObjects>("playerobjects")
    private val neighborCollection = db.getCollection<NeighborHistory>("neighborhistory")
    private val inventoryCollection = db.getCollection<Inventory>("inventory")

    init {
        Logger.info { "Initializing MongoDB..." }
        CoroutineScope(Dispatchers.IO).launch {
            setupCollections()
        }
    }

    private suspend fun setupCollections() {
        try {
            val count = plyCollection.estimatedDocumentCount()
            Logger.info { "MongoDB: User collection ready, contains $count users." }

            if (adminEnabled) {
                val adminDoc = plyCollection.find(Filters.eq("playerid", AdminData.PLAYER_ID)).firstOrNull()
                if (adminDoc == null) {
                    val start = getTimeMillis()
                    val doc = PlayerAccount.admin()
                    val obj = PlayerObjects.admin()
                    val neighbor = NeighborHistory.empty(AdminData.PLAYER_ID)
                    val inv = Inventory.admin()

                    plyCollection.insertOne(doc)
                    objCollection.insertOne(obj)
                    neighborCollection.insertOne(neighbor)
                    inventoryCollection.insertOne(inv)

                    Logger.info { "MongoDB: Admin account inserted in ${getTimeMillis() - start}ms" }
                } else {
                    Logger.info { "MongoDB: Admin account already exists." }
                }
            }

            setupIndexes()
        } catch (e: Exception) {
            Logger.error { "MongoDB: Failed during setupUserDocument: $e" }
        }
    }

    suspend fun setupIndexes() {
        plyCollection.createIndex(Indexes.text("profile.displayName"))
    }

    override suspend fun loadUserDocument(playerId: String): PlayerAccount? {
        return plyCollection.find(Filters.eq("playerId", playerId)).firstOrNull()
    }

    override suspend fun loadPlayerObjects(playerId: String): PlayerObjects? {
        return objCollection.find(Filters.eq("playerId", playerId)).firstOrNull()
    }

    override suspend fun loadNeighborHistory(playerId: String): NeighborHistory? {
        return neighborCollection.find(Filters.eq("playerId", playerId)).firstOrNull()
    }

    override suspend fun loadInventory(playerId: String): Inventory? {
        return inventoryCollection.find(Filters.eq("playerId", playerId)).firstOrNull()
    }

    override suspend fun getUserDocumentCollection(): MongoCollection<PlayerAccount> {
        return plyCollection
    }

    override suspend fun createUser(username: String, password: String): String {
        val pid = UUID.randomUUID().toString()
        val profile = UserProfile.default(username = username, pid = pid)
        val playerSrvId = UUID.randomUUID().toString()

        val meta = PlayerMetadata(
            playerId = pid,
            displayName = username,
            playerFlags = PlayerFlags.newgame(),
            playerSrvId = playerSrvId,
            leaderTitle = username,
            level = 1,
            xp = 0
        )

        val doc = PlayerAccount(
            playerId = pid,
            hashedPassword = hashPw(password),
            profile = profile,
            playerMetadata = meta,
            serverMetadata = ServerMetadata()
        )

        val obj = PlayerObjects.newgame(pid, username, playerSrvId)
        val neighbor = NeighborHistory.empty(pid)
        val inv = Inventory.newgame(pid)

        plyCollection.insertOne(doc)
        objCollection.insertOne(obj)
        neighborCollection.insertOne(neighbor)
        inventoryCollection.insertOne(inv)

        return pid
    }

    override suspend fun saveSurvivorAppearance(playerId: String, srvId: String, newAppearance: HumanAppearance) {
        val obj = requireNotNull(loadPlayerObjects(playerId)) { "PlayerObjects not found for playerId=$playerId" }
        val survivorIndex = obj.survivors.indexOfFirst { it.id == srvId }

        Logger.debug { "saveSurvivorAppearance: playerId=$playerId srvId=$srvId" }

        val path = "playerSave.playerObjects.survivors.$survivorIndex.appearance"
        val update = Updates.set(path, newAppearance)

        plyCollection.updateOne(Filters.eq("playerId", playerId), update)
    }

    override suspend fun updatePlayerFlags(playerId: String, flags: ByteArray) {
        Logger.debug { "updatePlayerFlags: playerId=$playerId flags=$flags" }

        val path = "playerSave.playerObjects.flags"
        val update = Updates.set(path, flags)

        plyCollection.updateOne(Filters.eq("playerId", playerId), update)
    }

    private fun hashPw(password: String): String {
        return Base64.encode(Bcrypt.hash(password, 10))
    }

    /**
     * Reset an entire UserDocument collection.
     */
    suspend fun resetUserCollection() {
        plyCollection.drop()
    }
}
