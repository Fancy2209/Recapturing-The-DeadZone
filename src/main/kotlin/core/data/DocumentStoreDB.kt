package dev.deadzone.core.data

/**
 * An implementation of [BigDB], uses [kotlin.document.store](https://github.com/lamba92/kotlin.document.store) library.
 */
class DocumentStoreDB: BigDB {
    override fun getCollection(name: String): String {
        return ""
    }
}
