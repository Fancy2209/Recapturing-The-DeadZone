package dev.deadzone

import com.mongodb.kotlin.client.coroutine.MongoClient
import dev.deadzone.module.*
import dev.deadzone.socket.Server
import io.ktor.server.application.*

fun main(args: Array<String>) {
    io.ktor.server.netty.EngineMain.main(args)
}

suspend fun Application.module() {
    configureDatabase()
    configureHTTP()
    configureRouting(db = Dependency.database)
    configureSerialization()
    configureLogging()
    configureSocket(db = Dependency.database)
}
