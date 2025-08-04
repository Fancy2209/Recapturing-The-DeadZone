package dev.deadzone.core.data

import com.github.lamba92.kotlin.document.store.core.DataStore
import com.github.lamba92.kotlin.document.store.core.KotlinDocumentStore
import com.github.lamba92.kotlin.document.store.core.find
import com.github.lamba92.kotlin.document.store.core.getObjectCollection
import dev.deadzone.core.auth.model.UserDocument
import dev.deadzone.module.Logger
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch

/**
 * An implementation of [BigDB], uses [kotlin.document.store](https://github.com/lamba92/kotlin.document.store) library.
 */
class DocumentStoreDB(store: DataStore) : BigDB {
    private val db = KotlinDocumentStore(store)
    private val USER_DOCUMENT_NAME = "userdocument"

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

            val adminDoc = docs.find("playerId", DummyData.PLAYER_ID).firstOrNull()
            if (adminDoc == null) {
                // likely very first-time DB setup
                val id = docs.insert(UserDocument.admin())
                Logger.info { "Inserted admin collection with id: $id" }
                val count = docs.size()
                Logger.info { "Now the collection contains $count users." }
            } else {
                Logger.info { "Admin collection found, is password right: ${adminDoc.hashedPassword == DummyData.PASSWORD}" }
            }
            Logger.info { "DocumentStoreDB is running fine..." }
        } catch (e: Exception) {
            Logger.error { "DocumentStoreDB fail during setupUserDocument: $e" }
        }
    }

    override fun doesUserExist(playerId: String): Boolean {
        return false
    }

    override fun createUser(playerId: String) {

    }

    suspend fun shutdown() {
        db.close()
    }

    suspend fun resetUserCollection(name: String = USER_DOCUMENT_NAME) {
        val collection = db.getObjectCollection<UserDocument>(name)
        collection.clear()
    }
}
