package dev.deadzone.user

import com.mongodb.client.model.Filters
import com.mongodb.client.model.Projections
import com.mongodb.client.model.Updates
import com.mongodb.kotlin.client.coroutine.MongoCollection
import com.toxicbakery.bcrypt.Bcrypt
import dev.deadzone.core.auth.model.UserProfile
import dev.deadzone.data.collection.PlayerAccount
import dev.deadzone.context.GlobalContext
import kotlinx.coroutines.flow.firstOrNull
import org.bson.Document
import kotlin.io.encoding.Base64

// TODO refactor with return result type and runMongoCatching
class PlayerAccountRepositoryMongo(val userCollection: MongoCollection<PlayerAccount>) : PlayerAccountRepository {
    override suspend fun doesUserExist(username: String): Boolean {
        return userCollection
            .find(Filters.eq("profile.displayName", username))
            .projection(null)
            .firstOrNull() != null
    }

    override suspend fun getUserDocByUsername(username: String): PlayerAccount? {
        return userCollection.find(Filters.eq("profile.displayName", username)).firstOrNull()
    }

    override suspend fun getUserDocByPlayerId(playerId: String): PlayerAccount? {
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
            GlobalContext.json.decodeFromString<UserProfile>(jsonString)
        }
    }

    override suspend fun updatePlayerAccount(
        playerId: String,
        account: PlayerAccount
    ) {
        userCollection.replaceOne(Filters.eq("playerId", playerId), account)
    }

    override suspend fun updateLastLogin(playerId: String, lastLogin: Long) {
        userCollection.updateOne(
            Filters.eq("playerId", playerId),
            Updates.set("profile.lastLogin", lastLogin)
        )
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
}
