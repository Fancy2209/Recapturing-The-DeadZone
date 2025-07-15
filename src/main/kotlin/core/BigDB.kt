package dev.deadzone.core

import com.mongodb.kotlin.client.coroutine.MongoCollection
import com.mongodb.kotlin.client.coroutine.MongoDatabase
import org.bson.Document

// Start MongoDB community edition (local)
// "C:\Program Files\MongoDB\Server\8.0\bin\mongod.exe" --dbpath="c:\data\db"
// use mongod if installed in PATH
// Must be cmd in Windows not powershell
class BigDB(private val db: MongoDatabase) {
    fun get(collection: String): MongoCollection<Document> {
        return db.getCollection(collection)
    }

    // temporary for easy db access
    fun getDB(): MongoDatabase {
        return db
    }
}
