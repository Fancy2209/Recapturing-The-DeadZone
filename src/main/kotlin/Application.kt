package dev.deadzone

import dev.deadzone.module.configureDatabases
import dev.deadzone.module.configureHTTP
import dev.deadzone.module.configureLogging
import dev.deadzone.module.configureRouting
import dev.deadzone.module.configureSerialization
import dev.deadzone.module.configureSockets
import io.ktor.server.application.*

fun main(args: Array<String>) {
    io.ktor.server.netty.EngineMain.main(args)
}

fun Application.module() {
    configureSockets()
    configureDatabases()
    configureHTTP()
    configureRouting()
    configureSerialization()
    configureLogging()
}
