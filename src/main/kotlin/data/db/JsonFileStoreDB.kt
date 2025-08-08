package dev.deadzone.data.db

import com.toxicbakery.bcrypt.Bcrypt
import dev.deadzone.core.auth.model.PlayerSave
import dev.deadzone.core.auth.model.ServerMetadata
import dev.deadzone.core.auth.model.UserDocument
import dev.deadzone.core.auth.model.UserProfile
import dev.deadzone.core.data.AdminData
import dev.deadzone.core.model.game.data.HumanAppearance
import dev.deadzone.module.LogConfigSocketToClient
import dev.deadzone.module.Logger
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext
import kotlinx.serialization.KSerializer
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.json.Json
import java.io.File
import java.nio.file.Files
import java.nio.file.StandardCopyOption
import java.util.*
import kotlin.io.encoding.Base64
import kotlin.reflect.KProperty1

/**
 * Simple in-memory, disk-backed database based on JSON files
 */
class JsonFileStoreDB(private val dbdir: File, private val json: Json, private val adminEnabled: Boolean) : BigDB {
    private val USER_DOCUMENT_NAME = "userdocument"
    private lateinit var udocs: JsonCollection<UserDocument>

    suspend fun setupUserDocument() {
        try {
            dbdir.mkdirs()
            udocs = JsonCollection(
                file = File(dbdir, "$USER_DOCUMENT_NAME.json"),
                serializer = UserDocument.serializer(),
                idField = UserDocument::playerId,
                json = json,
            )
            udocs.initialize()
            val count = udocs
            Logger.info { "JsonFileStoreDB: User collection ready, contains $count users." }

            if (adminEnabled) {
                val adminDoc = udocs.find { it.playerId == AdminData.PLAYER_ID }.firstOrNull()
                if (adminDoc == null) {
                    val doc = UserDocument.admin().copy(playerId = AdminData.PLAYER_ID)
                    udocs.insert(doc)
                    Logger.info { "JsonFileStoreDB: Admin account inserted with playerId=${doc.playerId}" }
                } else {
                    Logger.info { "JsonFileStoreDB: Admin account already exists." }
                }
            }
        } catch (e: Exception) {
            Logger.error { "JsonFileStoreDB: fail during setupUserDocument: $e" }
        }
    }

    override suspend fun doesUserExist(username: String): Boolean {
        return udocs.find { it.profile.displayName == username }.firstOrNull() != null
    }

    override suspend fun createUser(username: String, password: String): String {
        val pid = UUID.randomUUID().toString()
        val profile = UserProfile.default(username = username, pid = pid)

        val doc = UserDocument(
            playerId = pid,
            hashedPassword = hashPw(password),
            profile = profile,
            playerSave = PlayerSave.newgame(pid, username),
            metadata = ServerMetadata()
        )

        udocs.insert(doc)
        return pid
    }

    override suspend fun getUserDocByUsername(username: String): UserDocument? {
        return udocs.find { it.profile.displayName == username }.firstOrNull()
    }

    override suspend fun getUserDocByPlayerId(playerId: String): UserDocument? {
        return udocs.find { it.playerId == playerId }.firstOrNull()
    }

    override suspend fun getPlayerIdOfUsername(username: String): String? {
        return udocs.find { it.profile.displayName == username }.firstOrNull()?.playerId
    }

    override suspend fun getProfileOfPlayerId(playerId: String): UserProfile? {
        return udocs.find { it.playerId == playerId }.firstOrNull()?.profile
    }

    override suspend fun verifyCredentials(username: String, password: String): String? {
        val user = udocs.find { it.profile.displayName == username }.firstOrNull() ?: return null

        val hashed = user.hashedPassword
        val matches = Bcrypt.verify(password, Base64.decode(hashed))

        return if (matches) user.playerId else null
    }

    private fun hashPw(password: String): String {
        return Base64.encode(Bcrypt.hash(password, 10))
    }

    override suspend fun saveSurvivorAppearance(playerId: String, srvId: String, newAppearance: HumanAppearance) {
        udocs.update(playerId) { doc ->
            val updatedSurvivors = doc.playerSave.playerObjects.survivors.map { survivor ->
                if (survivor.id == srvId) {
                    survivor.copy(appearance = newAppearance)
                } else {
                    Logger.warn { "JsonFileStoreDB: No survivor update on saveSurvivorAppearance" }
                    survivor
                }
            }

            doc.copy(
                playerSave = doc.playerSave.copy(
                    playerObjects = doc.playerSave.playerObjects.copy(
                        survivors = updatedSurvivors
                    )
                )
            )
        }
    }

    override suspend fun updatePlayerFlags(playerId: String, flags: ByteArray) {
        udocs.update(playerId) { doc ->
            doc.copy(
                playerSave = doc.playerSave.copy(
                    playerObjects = doc.playerSave.playerObjects.copy(
                        flags = flags
                    )
                )
            )
        }
    }

    suspend fun shutdown() {
        udocs.shutdown()
    }

    /**
     * Reset an entire UserDocument collection by name.
     *
     * This is same as deleting the database folder (data/db)
     */
    suspend fun resetUserCollection() {
        udocs.drop()
    }
}

/**
 * Represent a document collection (similar to collection in MongoDB)
 *
 * Supports CRUD operation with mutex on write operation.
 */
class JsonCollection<T : Any>(
    private val file: File,
    private val serializer: KSerializer<T>,
    private val idField: KProperty1<T, String>,
    private val json: Json
) {
    private var documents: MutableList<T> = mutableListOf()
    private val mutex = Mutex()

    suspend fun initialize() {
        documents = load()
    }

    /**
     * Attempt to load the saved file to in-memory [documents]
     */
    private suspend fun load(): MutableList<T> = withContext(Dispatchers.IO) {
        if (!file.exists()) return@withContext mutableListOf()
        val text = file.readText()
        if (text.isBlank()) return@withContext mutableListOf()
        json.decodeFromString(ListSerializer(serializer), text).toMutableList()
    }

    /**
     * Save in-memory [documents] to disk [file].
     */
    private fun save() {
        val tempFile = File(file.parent, "${file.name}.tmp")
        val backupFile = File(file.parent, "${file.name}.bak")

        try {
            val encoded = json.encodeToString(ListSerializer(serializer), documents)

            // Write to temp file
            tempFile.writeText(encoded)

            // Backup the old file
            if (file.exists()) {
                file.copyTo(backupFile, overwrite = true)
            }

            // Try renaming/replacing the temp file to the real file.
            Files.move(
                tempFile.toPath(),
                file.toPath(),
                StandardCopyOption.REPLACE_EXISTING,
                StandardCopyOption.ATOMIC_MOVE
            )

            // Delete backup if succeed
            backupFile.delete()
        } catch (e: Exception) {
            Logger.error(LogConfigSocketToClient) { "JsonFileStoreDB: Save failed: ${e.message}" }

            // Attempt restore the real file from backup
            if (backupFile.exists()) {
                backupFile.copyTo(file, overwrite = true)
            }

            throw e
        } finally {
            // Clean temp file always
            tempFile.delete()
        }
    }


    suspend fun findAll(): List<T> = withContext(Dispatchers.Default) {
        documents.toList()
    }

    /**
     * Match a particular [T] based on the [predicate].
     *
     * @return List of [T] documents that matches the predicate.
     */
    suspend fun find(predicate: (T) -> Boolean): List<T> = withContext(Dispatchers.Default) {
        documents.filter(predicate)
    }

    /**
     * Insert a document and save to the disk file immediately.
     *
     * This will use the specified [idField] of the document [T]
     */
    suspend fun insert(document: T) = mutex.withLock {
        val id = idField.get(document)
        if (documents.any { idField.get(it) == id }) {
            throw IllegalArgumentException("Document with id $id already exists.")
        }
        documents.add(document)
        save()
    }

    /**
     * Update a document based on [id] with the given [updateFn] lambda.
     */
    suspend fun update(id: String, updateFn: (T) -> T) = mutex.withLock {
        val index = documents.indexOfFirst { idField.get(it) == id }
        if (index == -1) throw IllegalArgumentException("Document with id $id not found.")
        documents[index] = updateFn(documents[index])
        save()
    }

    /**
     * Delete a document based on [id].
     */
    suspend fun delete(id: String) = mutex.withLock {
        val removed = documents.removeIf { idField.get(it) == id }
        if (removed) save()
    }

    suspend fun upsert(document: T) = mutex.withLock {
        val id = idField.get(document)
        val index = documents.indexOfFirst { idField.get(it) == id }
        if (index >= 0) {
            documents[index] = document
        } else {
            documents.add(document)
        }
        save()
    }

    /**
     * Only save the in-memory documents to the disk file.
     */
    suspend fun shutdown() = mutex.withLock {
        save()
    }

    /**
     * Drop or clear the in-memory document including the disk file.
     */
    suspend fun drop() = mutex.withLock {
        withContext(Dispatchers.IO) {
            if (file.exists()) file.delete()
        }
        documents.clear()
    }

    /**
     * Get the size of documents.
     */
    fun size(): Int {
        return documents.size
    }
}
