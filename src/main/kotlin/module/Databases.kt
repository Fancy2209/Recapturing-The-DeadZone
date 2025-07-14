package dev.deadzone.module

import io.ktor.server.application.*
import org.jetbrains.exposed.sql.Database

fun Application.configureDatabases() {
//    Database.connect(
//        url = "jdbc:sqlite:/data:data.db",
//        user = "root",
//        driver = "org.sqlite.JDBC",
//        password = "",
//    )

    Dependency.database = Database.connect(
        url = "jdbc:sqlite:file:test?mode=memory&cache=shared",
        user = "root",
        driver = "org.sqlite.JDBC",
        password = "",
    )
}
