package dev.deadzone.module

import dev.deadzone.core.data.DocumentStoreDB

fun configureDatabase() {
    Dependency.database = DocumentStoreDB()
}
