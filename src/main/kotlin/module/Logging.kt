package dev.deadzone.module

import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.plugins.calllogging.CallLogging
import io.ktor.server.routing.RoutingContext
import java.io.File
import java.time.LocalTime
import java.time.format.DateTimeFormatter

fun Application.configureLogging() {
    install(CallLogging)
}

fun RoutingContext.logAPIInput(txt: Any?) {
    call.application.environment.log.info("Received [API ${call.parameters["path"]}]: $txt")
}

fun RoutingContext.logAPIOutput(txt: ByteArray?) {
    call.application.environment.log.info("Sent [API ${call.parameters["path"]}]: ${txt?.decodeToString()}")
}

object Logger {
    private val logDir = File("logs").apply { mkdirs() }
    private val errorLog = File(logDir, "write_error.log")
    private val missingAssetsLog = File(logDir, "missing_assets.log")
    private val unimplementedApiLog = File(logDir, "unimplemented_api.log")
    private val unimplementedSocketLog = File(logDir, "unimplemented_socket.log")

    fun logTo(file: File, message: Any?) {
        file.appendText("$message\n")
    }

    fun writeError(message: Any?) = logTo(errorLog, message)
    fun writeMissingAssets(message: Any?) = logTo(missingAssetsLog, message)
    fun unimplementedApi(message: Any?) = logTo(unimplementedApiLog, message)
    fun unimplementedSocket(message: Any?) = logTo(unimplementedSocketLog, message)

    fun socketPrint(txt: Any?) {
        println("[SOCKET/${getTime()}]: $txt")
    }

    fun print(txt: Any?) {
        println("[LOGGER/${getTime()}]: $txt")
    }

    private fun getTime(): String? {
        val currentTime = LocalTime.now()
        val formatter = DateTimeFormatter.ofPattern("HH:mm:ss")
        val formattedTime = currentTime.format(formatter)
        return formattedTime
    }
}
