package dev.deadzone

import dev.deadzone.module.configureDatabases
import dev.deadzone.module.configureFrameworks
import dev.deadzone.module.configureHTTP
import dev.deadzone.module.configureMonitoring
import dev.deadzone.module.configureRouting
import dev.deadzone.module.configureSockets
import io.ktor.server.application.*

fun main(args: Array<String>) {
    io.ktor.server.netty.EngineMain.main(args)
}

fun Application.module() {
    configureSockets()
    configureFrameworks()
    configureDatabases()
    configureMonitoring()
    configureHTTP()
    configureRouting()
}
