package dev.deadzone.module

import com.github.lamba92.kotlin.document.store.stores.leveldb.LevelDBStore
import com.mongodb.ConnectionString
import com.mongodb.KotlinCodecProvider
import com.mongodb.MongoClientSettings
import com.mongodb.kotlin.client.coroutine.MongoClient
import dev.deadzone.core.data.BigDBMongoImpl
import dev.deadzone.core.data.DocumentStoreDB
import kotlinx.coroutines.withTimeout
import org.bson.BsonReader
import org.bson.BsonWriter
import org.bson.Document
import org.bson.codecs.Codec
import org.bson.codecs.DecoderContext
import org.bson.codecs.EncoderContext
import org.bson.codecs.configuration.CodecRegistries
import java.io.File

// Start MongoDB community edition (local)
// "C:\Program Files\MongoDB\Server\8.0\bin\mongod.exe" --dbpath="c:\data\db"
// The path of 'mongod' depend on your installation
// Must be cmd in Windows not powershell
suspend fun configureDatabase(mongoUrl: String, adminEnabled: Boolean) {
    Logger.info { "Configuring database..." }
    try {
        withTimeout(5000L) {
            val mongoc = MongoClient.create(mongoUrl)
            val database = mongoc.getDatabase("admin")
            val commandResult = database.runCommand(Document("ping", 1))
            Logger.info { "MongoDB connection successful: $commandResult" }
            Dependency.database = BigDBMongoImpl(mongoc.getDatabase("tlsdz"), adminEnabled)
            return@withTimeout
        }
    } catch (e: Exception) {
        Logger.warn { "MongoDB connection failed: ${e.message} (this is expected if you don't have MongoDB installed). We will fallback to embedded DB instead." }
        File("data").mkdirs()
        val store = LevelDBStore.open("data/db")
        Dependency.database = DocumentStoreDB(store, adminEnabled).also { it.setupUserDocument() }
    }
}
