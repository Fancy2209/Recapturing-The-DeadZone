package dev.deadzone.module

import com.mongodb.kotlin.client.coroutine.MongoClient
import dev.deadzone.core.BigDB
import io.ktor.server.application.Application
import org.bson.Document

suspend fun Application.configureDatabase() {
    val db = MongoClient.create("mongodb://localhost:27017")
    Dependency.database = BigDB(db)

    try {
        val database = db.getDatabase("admin")
        val commandResult = database.runCommand(Document("ping", 1))
        println("MongoDB connection successful: $commandResult")
    } catch (e: Exception) {
        println("MongoDB connection failed: ${e.message}")
        throw e
    }
}
