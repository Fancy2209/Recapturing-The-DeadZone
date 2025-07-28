package dev.deadzone.module

import dev.deadzone.core.data.DocumentStoreDB
import io.ktor.server.application.Application

fun Application.configureDatabase() {
    Dependency.database = DocumentStoreDB()
}
