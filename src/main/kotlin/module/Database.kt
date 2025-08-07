package dev.deadzone.module

import com.github.lamba92.kotlin.document.store.stores.leveldb.LevelDBStore
import com.mongodb.kotlin.client.coroutine.MongoClient
import dev.deadzone.core.data.BigDBMongoImpl
import dev.deadzone.core.data.DocumentStoreDB
import kotlinx.coroutines.withTimeoutOrNull
import org.bson.Document
import java.io.File

// Start MongoDB community edition (local)
// "C:\Program Files\MongoDB\Server\8.0\bin\mongod.exe" --dbpath="c:\data\db"
// The path of 'mongod' depend on your installation
// Must be cmd in Windows not powershell
suspend fun configureDatabase(mongoUrl: String, adminEnabled: Boolean) {
    Logger.info { "Configuring database..." }

    val success = withTimeoutOrNull(5000L) {
        try {
            val mongoc = MongoClient.create(mongoUrl)
            val database = mongoc.getDatabase("admin")
            val commandResult = database.runCommand(Document("ping", 1))
            Logger.info { "MongoDB connection successful: $commandResult" }

            Dependency.database = BigDBMongoImpl(mongoc.getDatabase("tlsdz"), adminEnabled)
            true
        } catch (e: Exception) {
            Logger.warn { "MongoDB connection failed inside timeout: ${e.message}" }
            false
        }
    }

    if (success != true) {
        Logger.warn { "MongoDB connection failed or timed out. Falling back to embedded DB." }

        File("data").mkdirs()
        val store = LevelDBStore.open("data/db")
        Dependency.database = DocumentStoreDB(store, adminEnabled).also {
            it.setupUserDocument()
        }
    }
}
