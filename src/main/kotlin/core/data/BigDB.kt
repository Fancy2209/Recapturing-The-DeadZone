package dev.deadzone.core.data

/**
 * Representation of PlayerIO BigDB
 *
 * Implement DB as you need. We have: [DocumentStoreDB].
 */
interface BigDB {
    fun doesUserExist(playerId: String): Boolean
    fun createUser(playerId: String)
}
