package dev.deadzone.api.handler

import dev.deadzone.api.message.social.SocialProfile
import dev.deadzone.api.message.social.SocialRefreshOutput
import dev.deadzone.core.data.AdminData
import dev.deadzone.module.logInput
import dev.deadzone.module.logOutput
import dev.deadzone.module.pioFraming
import dev.deadzone.ServerContext
import io.ktor.http.HttpStatusCode
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.utils.io.*
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.encodeToByteArray
import kotlinx.serialization.protobuf.ProtoBuf

/**
 * SocialRefresh (API 601)
 *
 * Input: `SocialRefreshArgs` (empty)
 *
 * Output: `SocialRefreshOutput`
 */
@OptIn(ExperimentalSerializationApi::class)
suspend fun RoutingContext.socialRefresh(context: ServerContext, token: String) {
    val socialRefreshArgs = call.receiveChannel().toByteArray() // Actually no input is given

    logInput(socialRefreshArgs.decodeToString())

    // social features not implemented yet
    // likely we shouldn't bother with PIO publishing network and instead implement ourselves
    val pid = context.sessionManager.getPlayerId(token)!!
    val userProfile = context.db.getProfileOfPlayerId(pid)

    if (userProfile == null) {
        call.respond(HttpStatusCode.NotFound, "profile is not found")
        return
    }

    val socialRefreshOutput = if (pid == AdminData.PLAYER_ID) {
        SocialRefreshOutput.admin()
    } else {
        SocialRefreshOutput(
            myProfile = SocialProfile(
                userId = pid,
                displayName = userProfile.displayName,
                avatarUrl = userProfile.avatarUrl,
                lastOnline = userProfile.lastLogin,
                countryCode = userProfile.countryCode ?: "",
                userToken = token,
            ),
            friends = emptyList(),
            blocked = ""
        )
    }

    val encodedOutput = ProtoBuf.encodeToByteArray<SocialRefreshOutput>(socialRefreshOutput)

    logOutput(encodedOutput)

    call.respondBytes(encodedOutput.pioFraming())
}
