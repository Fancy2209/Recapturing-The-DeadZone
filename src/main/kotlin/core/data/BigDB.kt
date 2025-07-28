package dev.deadzone.core.data

/**
 * Representation of PlayerIO BigDB
 *
 * Implement DB as you need. We have: [DocumentStoreDB].
 */
interface BigDB {
    fun getCollection(name: String): String // just an example, will update later
}
