package dev.deadzone.module

import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.plugins.calllogging.CallLogging
import io.ktor.server.routing.RoutingContext
import java.io.File
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

object Logger {
    private val logDir = File("logs").apply { mkdirs() }
    private val errorLog = File(logDir, "write_error.log")
    private val unimplementedApiLog = File(logDir, "unimplemented_api.log")
    private val unimplementedSocketLog = File(logDir, "unimplemented_socket.log")

    fun logTo(file: File, message: Any) {
        file.appendText("$message\n")
    }

    fun writeError(message: Any) = logTo(errorLog, message)
    fun unimplementedApi(message: Any) = logTo(unimplementedApiLog, message)
    fun unimplementedSocket(message: Any) = logTo(unimplementedSocketLog, message)

    fun socketPrint(txt: Any) {
        println("[SOCKET]: $txt")
    }
}
