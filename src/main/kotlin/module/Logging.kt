package dev.deadzone.module

import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.plugins.calllogging.CallLogging
import io.ktor.server.routing.RoutingContext
import kotlin.text.decodeToString

fun Application.configureLogging() {
    install(CallLogging)
}

val RoutingContext.log get() = call.application.environment.log

private const val MAX_LOG_LENGTH = 300

fun RoutingContext.logApiMessage(message: Any, printFull: Boolean = false) {
    val msgString = message.toString()
    val truncated = if (msgString.length > MAX_LOG_LENGTH && !printFull) {
        msgString.take(MAX_LOG_LENGTH) + "... [truncated]"
    } else {
        msgString
    }
    call.application.environment.log.info("Received [API ${call.parameters["path"]}]: $truncated")
}

fun RoutingContext.logApiOutput(message: ByteArray, printFull: Boolean = false) {
    val msgString = try {
        message.decodeToString()
    } catch (e: Exception) {
        "[binary output not decodable]"
    }

    val truncated = if (msgString.length > MAX_LOG_LENGTH && !printFull) {
        msgString.take(MAX_LOG_LENGTH) + "... [truncated]"
    } else {
        msgString
    }

    call.application.environment.log.info("Sent [API ${call.parameters["path"]}]: $truncated")
}
