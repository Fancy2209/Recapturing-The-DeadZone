package dev.deadzone.module

import dev.deadzone.module.Logger.debug
import dev.deadzone.module.Logger.error
import dev.deadzone.module.Logger.info
import dev.deadzone.module.Logger.warn
import io.ktor.server.application.*
import io.ktor.server.plugins.calllogging.*
import io.ktor.server.routing.*
import kotlinx.serialization.Serializable
import java.io.File
import java.time.LocalTime
import java.time.format.DateTimeFormatter

const val MAX_LOG_LENGTH = 1000

fun Application.configureLogging() {
    install(CallLogging)
}

fun RoutingContext.logInput(txt: Any?) {
    info(
        src = LogSource.API,
        targets = setOf(LogTarget.PRINT),
        msg = { "Received [API ${call.parameters["path"]}]: $txt" }
    )
}

fun RoutingContext.logOutput(txt: ByteArray?) {
    info(
        src = LogSource.API,
        targets = setOf(LogTarget.PRINT),
        msg = { "Sent [API ${call.parameters["path"]}]: ${txt?.decodeToString()}" }
    )
}

/**
 * Use [debug], [info], [warn], and [error] to log a message, specifying a set of [LogTarget] (defaults to [LogTarget.PRINT] only)
 */
object Logger {
    var level: LogLevel = LogLevel.DEBUG

    private val logDir = File("logs").apply { mkdirs() }
    private val writeErrorLog = File(logDir, "write_error.log")
    private val missingAssetsLog = File(logDir, "missing_assets.log")
    private val unimplementedApiLog = File(logDir, "unimplemented_api.log")
    private val unimplementedSocketLog = File(logDir, "unimplemented_socket.log")

    private val logFileMap = mapOf(
        LogFile.WRITE_ERROR to writeErrorLog,
        LogFile.MISSING_ASSETS to missingAssetsLog,
        LogFile.UNIMPLEMENTED_API to unimplementedApiLog,
        LogFile.UNIMPLEMENTED_SOCKET to unimplementedSocketLog,
    )

    private fun log(
        src: LogSource = LogSource.ANY,
        targets: Set<LogTarget> = setOf(LogTarget.PRINT),
        level: LogLevel = LogLevel.DEBUG,
        msg: () -> String,
        logFull: Boolean = false
    ) {
        if (level < this.level) return

        val srcName = if (src != LogSource.ANY) src.name else ""
        var msgString = msg()

        if (msgString.length > MAX_LOG_LENGTH && !logFull) {
            msgString = msgString.take(MAX_LOG_LENGTH) + "... [truncated]"
        }

        val logMessage = "[LOGGER|$srcName|${getTimestamp()}] [${level.name}]: $msgString"

        targets.forEach { target ->
            when (target) {
                is LogTarget.PRINT -> {
                    println(logMessage)
                }

                is LogTarget.FILE -> {
                    val targetFile = logFileMap[target.file]
                    if (targetFile != null) {
                        targetFile.appendText("$logMessage\n")
                    } else {
                        println("[LOGGER|ERROR] Unknown log file target: ${target.file}")
                    }
                }

                is LogTarget.CLIENT -> {
                    // TO-DO send to websocket
                }
            }
        }
    }

    fun debug(msg: String) = debug { msg }
    fun debug(
        src: LogSource = LogSource.ANY,
        targets: Set<LogTarget> = setOf(LogTarget.PRINT),
        logFull: Boolean = false,
        msg: () -> String
    ) {
        log(src, targets, LogLevel.DEBUG, msg, logFull)
    }

    fun info(msg: String) = info { msg }
    fun info(
        src: LogSource = LogSource.ANY,
        targets: Set<LogTarget> = setOf(LogTarget.PRINT),
        logFull: Boolean = false,
        msg: () -> String
    ) {
        log(src, targets, LogLevel.INFO, msg, logFull)
    }

    fun warn(msg: String) = warn { msg }
    fun warn(
        src: LogSource = LogSource.ANY,
        targets: Set<LogTarget> = setOf(LogTarget.PRINT),
        logFull: Boolean = false,
        msg: () -> String
    ) {
        log(src, targets, LogLevel.WARN, msg, logFull)
    }

    fun error(msg: String) = error { msg }
    fun error(
        src: LogSource = LogSource.ANY,
        targets: Set<LogTarget> = setOf(LogTarget.PRINT),
        logFull: Boolean = false,
        msg: () -> String
    ) {
        log(src, targets, LogLevel.ERROR, msg, logFull)
    }
}

fun getTimestamp(): String {
    val currentTime = LocalTime.now()
    val formatter = DateTimeFormatter.ofPattern("HH:mm:ss")
    val formattedTime = currentTime.format(formatter)
    return formattedTime.toString()
}

enum class LogLevel() {
    DEBUG, INFO, WARN, ERROR
}

sealed class LogTarget {
    object PRINT : LogTarget()
    object CLIENT : LogTarget()
    data class FILE(val file: LogFile = LogFile.WRITE_ERROR) : LogTarget()
}

enum class LogFile {
    MISSING_ASSETS, UNIMPLEMENTED_API, UNIMPLEMENTED_SOCKET, WRITE_ERROR
}

enum class LogSource {
    SOCKET, API, ANY
}

@Serializable
data class LogMessage(
    val level: LogLevel,
    val msg: String,
)
