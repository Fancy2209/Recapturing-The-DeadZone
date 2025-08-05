package dev.deadzone.core.data

import com.github.lamba92.kotlin.document.store.core.DataStore
import com.github.lamba92.kotlin.document.store.core.KotlinDocumentStore
import com.github.lamba92.kotlin.document.store.core.ObjectCollection
import com.github.lamba92.kotlin.document.store.core.find
import com.github.lamba92.kotlin.document.store.core.getObjectCollection
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
import java.util.UUID
import kotlin.io.encoding.Base64

/**
 * An implementation of [BigDB], uses [kotlin.document.store](https://github.com/lamba92/kotlin.document.store) library.
 */
class DocumentStoreDB(store: DataStore, private val adminEnabled: Boolean) : BigDB {
    private val db = KotlinDocumentStore(store)
    private val USER_DOCUMENT_NAME = "userdocument"
    private lateinit var udocs: ObjectCollection<UserDocument>

    init {
        CoroutineScope(Dispatchers.IO).launch {
            setupUserDocument()
        }
    }

    private suspend fun setupUserDocument() {
        try {
            val docs = db.getObjectCollection<UserDocument>(USER_DOCUMENT_NAME)
            val count = docs.size()
            Logger.info { "User collection ready, contains $count users." }
            udocs = docs

            if (adminEnabled) {
                val adminDoc = docs.find("playerId", AdminData.PLAYER_ID).firstOrNull()
                if (adminDoc == null) {
                    val doc = UserDocument.admin().copy(playerId = AdminData.PLAYER_ID)
                    docs.insert(doc)
                    Logger.info { "Admin account inserted with playerId=${doc.playerId}" }
                } else {
                    Logger.info { "Admin account already exists." }
                }
            }

            setupIndexes()
        } catch (e: Exception) {
            Logger.error { "DocumentStoreDB fail during setupUserDocument: $e" }
        }
    }

    /**
     * Access of nested objects can be done through dot notation.
     * It is required to setup the index for accessing the object nested structure.
     */
    private suspend fun setupIndexes() {
        udocs.createIndex("profile.displayName")
    }

    override suspend fun doesUserExist(username: String): Boolean {
        return udocs.find("profile.displayName", username).firstOrNull() != null
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

        udocs.insert(doc)
        return pid
    }

    override suspend fun getUserDocByUsername(username: String): UserDocument? {
        return udocs.find("profie.displayName", username).firstOrNull()
    }

    override suspend fun getPlayerIdOfUsername(username: String): String? {
        return udocs.find("profie.displayName", username).firstOrNull()?.playerId
    }

    override suspend fun verifyCredentials(username: String, password: String): String? {
        val user = udocs.find("profile.displayName", username).firstOrNull() ?: return null

        val hashed = user.hashedPassword
        val matches = Bcrypt.verify(password, Base64.decode(hashed))

        return if (matches) user.playerId else null
    }

    private fun hashPw(password: String): String {
        return Base64.encode(Bcrypt.hash(password, 10))
    }

    suspend fun shutdown() {
        db.close()
    }

    /**
     * Reset an entire UserDocument collection by name.
     *
     * This is same as deleting the database folder (data/db)
     */
    suspend fun resetUserCollection(name: String = USER_DOCUMENT_NAME) {
        val collection = db.getObjectCollection<UserDocument>(name)
        collection.clear()
    }
}
