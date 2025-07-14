package dev.deadzone.module

import org.jetbrains.exposed.sql.Database

object Dependency {
    lateinit var database: Database
}
