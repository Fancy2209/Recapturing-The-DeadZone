package dev.deadzone.module

import com.mongodb.kotlin.client.coroutine.MongoClient
import dev.deadzone.core.BigDB
import io.ktor.server.application.Application
import org.bson.Document

// Start MongoDB community edition (local)
// "C:\Program Files\MongoDB\Server\8.0\bin\mongod.exe" --dbpath="c:\data\db"
// use mongod if installed in PATH
// Must be cmd in Windows not powershell
suspend fun Application.configureDatabase() {
    val mongoc = MongoClient.create("mongodb://localhost:27017")

    try {
        val database = mongoc.getDatabase("admin")
        val commandResult = database.runCommand(Document("ping", 1))
        println("MongoDB connection successful: $commandResult")
    } catch (e: Exception) {
        println("MongoDB connection failed: ${e.message}")
        throw e
    }

    Dependency.database = BigDB(mongoc.getDatabase("tlsdz"))
}
