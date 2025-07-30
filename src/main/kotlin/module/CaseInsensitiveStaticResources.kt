package dev.deadzone.module

import io.github.classgraph.ClassGraph
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.caseInsensitiveStaticResources(baseUrl: String, resourceRoot: String = "static") {
    val resourceMap = scanClasspathResources(resourceRoot)

    for (key in resourceMap.keys) {
//        Logger.debug(LogConfigAPI) { "$key -> ${resourceMap[key]}" }
    }

    get("$baseUrl/{...}") {
        val rawPath = call.request.path().replace(Regex("/+"), "/")

        val lookupKey = if (rawPath.startsWith("/game/data")) {
            "static" + rawPath.lowercase()
        } else {
            "static$rawPath"
        }

        val actualResourcePath = resourceMap[lookupKey]

//        Logger.debug(LogConfigAPI) { "🔶 Serving $rawPath" }
//        Logger.debug(LogConfigAPI) { "lookupKey: $lookupKey" }
//        Logger.debug(LogConfigAPI) { "actual: $actualResourcePath" }

        if (actualResourcePath != null) {
            val resource = Application::class.java.getResourceAsStream("/$actualResourcePath")
            if (resource != null) {
                val contentType = ContentType.defaultForFileExtension(
                    rawPath.substringAfterLast('.', "")
                )
                return@get call.respondBytes(resource.readBytes(), contentType)
            }
        }

        call.respond(HttpStatusCode.NotFound)
    }
}

fun scanClasspathResources(basePackage: String): Map<String, String> {
    val map = mutableMapOf<String, String>()

    ClassGraph().acceptPaths(basePackage).scan().use { scanResult ->
        for (res in scanResult.allResources) {
            val fullPath = res.path                      // e.g. static/game/data/models/...
            map[fullPath] = fullPath                     // exact‑case key  ( still useful )
            if (fullPath.startsWith("$basePackage/game/data/", ignoreCase = true)) {
                map[fullPath.lowercase()] = fullPath     // case‑insensitive alias
            }
        }
    }
    return map
}
