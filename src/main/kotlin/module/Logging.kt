package dev.deadzone.module

import dev.deadzone.module.Logger.info
import io.ktor.server.application.*
import io.ktor.server.plugins.calllogging.*
import io.ktor.server.routing.*
import io.ktor.server.websocket.DefaultWebSocketServerSession
import io.ktor.util.date.getTimeMillis
import io.ktor.websocket.Frame
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.io.File
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.Collections
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.CopyOnWriteArraySet
import kotlin.time.Duration.Companion.seconds

fun Application.configureLogging() {
    install(CallLogging)
}

fun RoutingContext.logInput(txt: Any?, logFull: Boolean = false) {
    info(LogSource.API, logFull = logFull) { "Received [API ${call.parameters["path"]}]: $txt" }
}

fun RoutingContext.logOutput(txt: ByteArray?, logFull: Boolean = false) {
    info(LogSource.API, logFull = logFull) { "Sent [API ${call.parameters["path"]}]: ${txt?.decodeToString()}" }
}

/**
 * Custom logging utility object that supports multiple log levels, output targets, and log configurations.
 *
 * ### Usage
 *
 * There are four main styles of usage:
 *
 * **1)** Simple logging (direct message):
 *
 * ```kotlin
 * Logger.debug("Simple debug message")
 * Logger.info("Something happened")
 * ```
 *
 * **2)** Lazy-evaluated logging (only evaluated if level is enabled):
 *
 * ```kotlin
 * Logger.debug { "Expensive to compute: ${'$'}{expensiveCalculation()}" }
 * ```
 *
 * **3)** Structured logging using [LogConfig] presets:
 *
 * ```kotlin
 * Logger.info(LogConfigAPIToClient) { "API response sent to client" }
 * Logger.error(LogConfigSocketError) { "Socket connection failed" }
 * ```
 *
 * **4)** Override truncation behavior:
 *
 * ```kotin
 * Logger.info(LogConfigAPIToClient, forceLogFull = true) { "Very long message..." }
 * ```
 *
 * ### Features
 *
 * - Four log levels: [LogLevel.DEBUG], [LogLevel.INFO], [LogLevel.WARN], [LogLevel.ERROR]
 * - Output to:
 *     - Standard output via `[LogTarget.PRINT]`
 *     - Log files via `[LogTarget.FILE]`
 *     - WebSocket via `[LogTarget.CLIENT]`
 * - Message truncation unless `logFull = true`
 * - Timestamped messages (`HH:mm:ss`)
 *
 * ### Configuration
 *
 * - Set the global log level with `Logger.level`
 * - Use [LogConfig] presets for common sources/targets
 * - Extend [LogFile] and update `logFileMap` if needed
 *
 * ### Example custom config
 *
 * ```kotlin
 * val LogConfigCustom = LogConfig(
 *     src = LogSource.API,
 *     targets = setOf(LogTarget.PRINT, LogTarget.FILE(LogFile.API_SERVER_ERROR)),
 *     logFull = false
 * )
 * ```
 */
object Logger {
    var level: LogLevel = LogLevel.DEBUG
    val connectedDebugClients = ConcurrentHashMap<String, DefaultWebSocketServerSession>()
    val sessionLogBuffers = ConcurrentHashMap<String, ArrayDeque<LogMessage>>()

    private val logDir = File("logs").apply { mkdirs() }
    private val clientWriteError = File(logDir, "client_write_error.log")
    private val assetsError = File(logDir, "assets_error.log")
    private val apiServerError = File(logDir, "api_server_error.log")
    private val socketServerError = File(logDir, "socket_server_error.log")

    private val logFileMap = mapOf(
        LogFile.CLIENT_WRITE_ERROR to clientWriteError,
        LogFile.ASSETS_ERROR to assetsError,
        LogFile.API_SERVER_ERROR to apiServerError,
        LogFile.SOCKET_SERVER_ERROR to socketServerError,
    )

    private val MAX_BUFFER_SIZE = 100
    private val MAX_LOG_LENGTH = 500

    private fun log(
        src: LogSource = LogSource.SOCKET,
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

        val logMessage = "[LOGGER | $srcName | ${getTimestamp()}] [${level.name}]: $msgString"

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
                        println("Unknown log file target: ${target.file}")
                    }
                }

                is LogTarget.CLIENT -> {
                    val logMsg = LogMessage(level, logMessage)

                    CoroutineScope(Dispatchers.IO).launch {
                        for ((clientId, session) in connectedDebugClients) {
                            val buffer = sessionLogBuffers.getOrPut(clientId) { ArrayDeque() }

                            buffer.addLast(logMsg)
                            if (buffer.size > MAX_BUFFER_SIZE) buffer.removeFirst()

                            try {
                                session.send(Frame.Text(Json.encodeToString(logMsg)))
                            } catch (e: Exception) {
                                println("Failed to send log to client $session: $e")
                                connectedDebugClients.remove(clientId)
                                sessionLogBuffers.remove(clientId)
                            }
                        }
                    }
                }
            }
        }
    }

    fun debug(msg: String) = debug { msg }
    fun debug(config: LogConfig, forceLogFull: Boolean? = null, msg: () -> String) {
        val logFull = forceLogFull ?: config.logFull
        debug(config.src, config.targets, logFull) { msg() }
    }

    fun debug(
        src: LogSource = LogSource.SOCKET,
        targets: Set<LogTarget> = setOf(LogTarget.PRINT),
        logFull: Boolean = false,
        msg: () -> String
    ) {
        log(src, targets, LogLevel.DEBUG, msg, logFull)
    }

    fun info(msg: String) = info { msg }
    fun info(config: LogConfig, forceLogFull: Boolean? = null, msg: () -> String) {
        val logFull = forceLogFull ?: config.logFull
        info(config.src, config.targets, logFull) { msg() }
    }

    fun info(
        src: LogSource = LogSource.SOCKET,
        targets: Set<LogTarget> = setOf(LogTarget.PRINT),
        logFull: Boolean = false,
        msg: () -> String
    ) {
        log(src, targets, LogLevel.INFO, msg, logFull)
    }

    fun warn(msg: String) = warn { msg }
    fun warn(config: LogConfig, forceLogFull: Boolean? = null, msg: () -> String) {
        val logFull = forceLogFull ?: config.logFull
        warn(config.src, config.targets, logFull) { msg() }
    }

    fun warn(
        src: LogSource = LogSource.SOCKET,
        targets: Set<LogTarget> = setOf(LogTarget.PRINT),
        logFull: Boolean = false,
        msg: () -> String
    ) {
        log(src, targets, LogLevel.WARN, msg, logFull)
    }

    fun error(msg: String) = error { msg }
    fun error(config: LogConfig, forceLogFull: Boolean? = null, msg: () -> String) {
        val logFull = forceLogFull ?: config.logFull
        error(config.src, config.targets, logFull) { msg() }
    }

    fun error(
        src: LogSource = LogSource.SOCKET,
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
    data class FILE(val file: LogFile = LogFile.CLIENT_WRITE_ERROR) : LogTarget()
}

enum class LogFile {
    CLIENT_WRITE_ERROR, ASSETS_ERROR, API_SERVER_ERROR, SOCKET_SERVER_ERROR,
}

enum class LogSource {
    SOCKET, API, ANY
}

data class LogConfig(
    val src: LogSource,
    val targets: Set<LogTarget> = setOf(LogTarget.PRINT),
    val logFull: Boolean = false
)

val LogConfigWriteError = LogConfig(
    src = LogSource.API,
    targets = setOf(LogTarget.PRINT, LogTarget.FILE(LogFile.CLIENT_WRITE_ERROR), LogTarget.CLIENT),
    logFull = true
)

val LogConfigAPIToClient = LogConfig(
    src = LogSource.API,
    targets = setOf(LogTarget.PRINT, LogTarget.CLIENT),
    logFull = false
)

val LogConfigAPIError = LogConfig(
    src = LogSource.API,
    targets = setOf(LogTarget.PRINT, LogTarget.FILE(LogFile.API_SERVER_ERROR), LogTarget.CLIENT),
    logFull = true
)

val LogConfigSocketToClient = LogConfig(
    src = LogSource.SOCKET,
    targets = setOf(LogTarget.PRINT, LogTarget.CLIENT),
    logFull = false
)

val LogConfigSocketError = LogConfig(
    src = LogSource.SOCKET,
    targets = setOf(LogTarget.PRINT, LogTarget.FILE(LogFile.SOCKET_SERVER_ERROR), LogTarget.CLIENT),
    logFull = true
)

val LogConfigAssetsError = LogConfig(
    src = LogSource.ANY,
    targets = setOf(LogTarget.PRINT, LogTarget.FILE(LogFile.ASSETS_ERROR), LogTarget.CLIENT),
    logFull = true
)

@Serializable
data class LogMessage(
    val level: LogLevel,
    val msg: String,
)
