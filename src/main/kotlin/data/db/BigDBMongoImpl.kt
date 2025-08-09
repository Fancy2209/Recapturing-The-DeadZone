package dev.deadzone.core.data

import com.mongodb.client.model.Filters
import com.mongodb.client.model.Indexes
import com.mongodb.client.model.Projections
import com.mongodb.client.model.Updates
import com.mongodb.kotlin.client.coroutine.MongoCollection
import com.mongodb.kotlin.client.coroutine.MongoDatabase
import com.toxicbakery.bcrypt.Bcrypt
import dev.deadzone.core.auth.model.ServerMetadata
import dev.deadzone.data.collection.UserDocument
import dev.deadzone.core.auth.model.UserProfile
import dev.deadzone.core.model.data.PlayerFlags
import dev.deadzone.core.model.game.data.HumanAppearance
import dev.deadzone.data.collection.Inventory
import dev.deadzone.data.collection.NeighborHistory
import dev.deadzone.data.collection.PlayerMetadata
import dev.deadzone.data.collection.PlayerObjects
import dev.deadzone.data.db.BigDB
import dev.deadzone.module.Dependency
import dev.deadzone.module.Logger
import io.ktor.util.date.getTimeMillis
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import org.bson.Document
import java.util.*
import kotlin.io.encoding.Base64

class BigDBMongoImpl(db: MongoDatabase, private val adminEnabled: Boolean) : BigDB {
    private val userCollection = db.getCollection<UserDocument>("userdocument")
    private val metaCollection = db.getCollection<PlayerMetadata>("playermetadata")
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
            val count = userCollection.estimatedDocumentCount()
            Logger.info { "MongoDB: User collection ready, contains $count users." }

            if (adminEnabled) {
                val adminDoc = userCollection.find(Filters.eq("playerid", AdminData.PLAYER_ID)).firstOrNull()
                if (adminDoc == null) {
                    val start = getTimeMillis()
                    val doc = UserDocument.admin()
                    val meta = PlayerMetadata.admin()
                    val obj = PlayerObjects.admin()
                    val neighbor = NeighborHistory.empty(AdminData.PLAYER_ID)
                    val inv = Inventory.admin()

                    userCollection.insertOne(doc)
                    metaCollection.insertOne(meta)
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
        userCollection.createIndex(Indexes.text("profile.displayName"))
    }

    override suspend fun loadUserDocument(playerId: String): UserDocument? {
        return userCollection.find(Filters.eq("playerId", playerId)).firstOrNull()
    }

    override suspend fun loadPlayerMetadata(playerId: String): PlayerMetadata? {
        return metaCollection.find(Filters.eq("playerId", playerId)).firstOrNull()
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

    override suspend fun getUserDocumentCollection(): MongoCollection<UserDocument> {
        return userCollection
    }

    override suspend fun createUser(username: String, password: String): String {
        val pid = UUID.randomUUID().toString()
        val profile = UserProfile.default(username = username, pid = pid)

        val doc = UserDocument(
            playerId = pid,
            hashedPassword = hashPw(password),
            profile = profile,
            metadata = ServerMetadata()
        )

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

        val obj = PlayerObjects.newgame(pid, username, playerSrvId)
        val neighbor = NeighborHistory.empty(pid)
        val inv = Inventory.newgame(pid)

        userCollection.insertOne(doc)
        metaCollection.insertOne(meta)
        objCollection.insertOne(obj)
        neighborCollection.insertOne(neighbor)
        inventoryCollection.insertOne(inv)

        return pid
    }

    override suspend fun doesUserExist(username: String): Boolean {
        return userCollection
            .find(Filters.eq("profile.displayName", username))
            .projection(null)
            .firstOrNull() != null
    }

    override suspend fun getUserDocByUsername(username: String): UserDocument? {
        return userCollection.find(Filters.eq("profile.displayName", username)).firstOrNull()
    }

    override suspend fun getUserDocByPlayerId(playerId: String): UserDocument? {
        return userCollection.find(Filters.eq("playerId", playerId)).firstOrNull()
    }

    override suspend fun getPlayerIdOfUsername(username: String): String? {
        return userCollection
            .find(Filters.eq("profile.displayName", username))
            .projection(Projections.include("playerId"))
            .firstOrNull()
            ?.playerId
    }

    override suspend fun getProfileOfPlayerId(playerId: String): UserProfile? {
        val doc = userCollection
            .withDocumentClass<Document>()
            .find(Filters.eq("playerId", playerId))
            .projection(Projections.include("profile"))
            .firstOrNull()

        val profileDoc = doc?.get("profile") as? Document
        return profileDoc?.let {
            val jsonString = it.toJson()
            Dependency.json.decodeFromString<UserProfile>(jsonString)
        }
    }

    override suspend fun verifyCredentials(username: String, password: String): String? {
        val doc = userCollection
            .withDocumentClass<Document>()
            .find(Filters.eq("profile.displayName", username))
            .projection(Projections.include("hashedPassword", "playerId"))
            .firstOrNull()

        if (doc == null) return null

        val hashed = doc.getString("hashedPassword")
        val playerId = doc.getString("playerId")
        val matches = Bcrypt.verify(password, Base64.decode(hashed))

        return if (matches) playerId else null
    }

    private fun hashPw(password: String): String {
        return Base64.encode(Bcrypt.hash(password, 10))
    }

    override suspend fun saveSurvivorAppearance(playerId: String, srvId: String, newAppearance: HumanAppearance) {
        val obj = requireNotNull(loadPlayerObjects(playerId)) { "PlayerObjects not found for playerId=$playerId" }
        val survivorIndex = obj.survivors.indexOfFirst { it.id == srvId }

        Logger.debug { "saveSurvivorAppearance: playerId=$playerId srvId=$srvId" }

        val path = "playerSave.playerObjects.survivors.$survivorIndex.appearance"
        val update = Updates.set(path, newAppearance)

        userCollection.updateOne(Filters.eq("playerId", playerId), update)
    }

    override suspend fun updatePlayerFlags(playerId: String, flags: ByteArray) {
        Logger.debug { "updatePlayerFlags: playerId=$playerId flags=$flags" }

        val path = "playerSave.playerObjects.flags"
        val update = Updates.set(path, flags)

        userCollection.updateOne(Filters.eq("playerId", playerId), update)
    }

    /**
     * Reset an entire UserDocument collection.
     */
    suspend fun resetUserCollection() {
        userCollection.drop()
    }
}
