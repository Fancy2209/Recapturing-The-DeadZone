package dev.deadzone.module

import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.plugins.calllogging.CallLogging
import io.ktor.server.routing.RoutingContext

fun Application.configureLogging() {
    install(CallLogging)
}

val RoutingContext.log get() = call.application.environment.log
