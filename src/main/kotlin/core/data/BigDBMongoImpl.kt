package dev.deadzone.core.data

import com.mongodb.client.model.Filters
import com.mongodb.client.model.Indexes
import com.mongodb.kotlin.client.coroutine.MongoDatabase
import com.toxicbakery.bcrypt.Bcrypt
import dev.deadzone.core.auth.model.PlayerSave
import dev.deadzone.core.auth.model.ServerMetadata
import dev.deadzone.core.auth.model.UserDocument
import dev.deadzone.core.auth.model.UserProfile
import dev.deadzone.module.Logger
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import org.bson.BsonReader
import org.bson.BsonWriter
import org.bson.codecs.Codec
import org.bson.codecs.DecoderContext
import org.bson.codecs.EncoderContext
import java.util.*
import kotlin.io.encoding.Base64

class BigDBMongoImpl(db: MongoDatabase, private val adminEnabled: Boolean) : BigDB {
    private val USER_DOCUMENT_NAME = "userdocument"
    private val udocs = db.getCollection<UserDocument>(USER_DOCUMENT_NAME)

    init {
        Logger.info { "Initializing MongoDB..." }
        CoroutineScope(Dispatchers.IO).launch {
            setupUserDocument()
        }
    }

    private suspend fun setupUserDocument() {
        try {
            val count = udocs.estimatedDocumentCount()
            Logger.info { "MongoDB: User collection ready, contains $count users." }

            if (adminEnabled) {
                val adminDoc = udocs.find(Filters.eq("playerid", AdminData.PLAYER_ID)).firstOrNull()
                if (adminDoc == null) {
                    val doc = UserDocument.admin().copy(playerId = AdminData.PLAYER_ID)
                    udocs.insertOne(doc)
                    Logger.info { "MongoDB: Admin account inserted with playerId=${doc.playerId}" }
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
        udocs.createIndex(Indexes.text("profile.displayName"))
    }

    override suspend fun createUser(username: String, password: String): String {
        val pid = UUID.randomUUID().toString()
        val profile = UserProfile(
            playerId = pid,
            email = "",
            displayName = username,
            avatarUrl = "",
        )

        val doc = UserDocument(
            playerId = pid,
            hashedPassword = hashPw(password),
            profile = profile,
            playerSave = PlayerSave.newgame(),
            metadata = ServerMetadata()
        )

        udocs.insertOne(doc)
        return pid
    }

    override suspend fun doesUserExist(username: String): Boolean {
        return udocs.find(Filters.eq("profile.displayName", username)).firstOrNull() != null
    }

    override suspend fun getUserDocByUsername(username: String): UserDocument? {
        return udocs.find(Filters.eq("profile.displayName", username)).firstOrNull()
    }

    override suspend fun getPlayerIdOfUsername(username: String): String? {
        return udocs.find(Filters.eq("profile.displayName", username)).firstOrNull()?.playerId
    }

    override suspend fun verifyCredentials(username: String, password: String): String? {
        val user = udocs.find(Filters.eq("profile.displayName", username)).firstOrNull() ?: return null

        val hashed = user.hashedPassword
        val matches = Bcrypt.verify(password, Base64.decode(hashed))

        return if (matches) user.playerId else null
    }

    private fun hashPw(password: String): String {
        return Base64.encode(Bcrypt.hash(password, 10))
    }

    /**
     * Reset an entire UserDocument collection.
     */
    suspend fun resetUserCollection() {
        udocs.drop()
    }
}
