package dev.deadzone.module

import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.plugins.calllogging.CallLogging
import io.ktor.server.routing.RoutingContext

fun Application.configureLogging() {
    install(CallLogging)
}

val RoutingContext.log get() = call.application.environment.log

fun RoutingContext.logApiMessage(message: Any) {
    call.application.environment.log.info("Received [API ${call.parameters["path"]}]: $message")
}

fun RoutingContext.logApiOutput(message: ByteArray) {
    call.application.environment.log.info("Sent [API ${call.parameters["path"]}]: ${message.decodeToString()}")
}
