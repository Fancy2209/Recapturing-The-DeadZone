package dev.deadzone.core

import com.mongodb.kotlin.client.coroutine.MongoCollection
import com.mongodb.kotlin.client.coroutine.MongoDatabase
import org.bson.Document

class BigDB(private val db: MongoDatabase) {
    fun get(collection: String): MongoCollection<Document> {
        return db.getCollection(collection)
    }
}
