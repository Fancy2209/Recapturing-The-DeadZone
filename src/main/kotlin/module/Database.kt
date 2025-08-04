package dev.deadzone.module

import com.github.lamba92.kotlin.document.store.stores.leveldb.LevelDBStore
import dev.deadzone.core.data.DocumentStoreDB
import java.io.File

fun configureDatabase() {
    File("data").mkdirs()
    val store = LevelDBStore.open("data/db")
    Dependency.database = DocumentStoreDB(store)
}
