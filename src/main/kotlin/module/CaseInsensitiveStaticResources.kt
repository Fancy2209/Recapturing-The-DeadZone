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
        // Logger.print("$key -> ${resourceMap[key]}")
    }

    get("$baseUrl/{...}") {
        val rawPath = call.request.path().replace(Regex("/+"), "/")

        val lookupKey = if (rawPath.equals("/game/core.swf", ignoreCase = true)) {
            return@get call.respond(HttpStatusCode.NotFound)
        } else if (rawPath.startsWith("/game/data")) {
            "static" + rawPath.lowercase()
        } else {
            "static$rawPath"
        }

        val actualResourcePath = resourceMap[lookupKey]

        // Logger.print("🔶 Serving $rawPath")
        // Logger.print("lookupKey: $lookupKey")
        // Logger.print("actual: $actualResourcePath")

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

    // Special case for serving / (root) as index.html (case-sensitive)
    get(baseUrl) {
        // Logger.print("🔶 Serving index.html")
        val indexPath = "$resourceRoot/index.html"
        val stream =
            resourceMap[indexPath]?.let { this::class.java.classLoader.getResourceAsStream(it) }

        if (stream != null) {
            return@get call.respondBytes(stream.readBytes(), ContentType.Text.Html)
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
