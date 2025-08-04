package dev.deadzone.core.auth.model

import kotlinx.serialization.Serializable

/**
 * Player login state is needed for [dev.deadzone.socket.handler.JoinHandler] result
 *
 * Structure still empty and assumption. See Network.as onGameReady and onPlayerDataLoaded
 */
@Serializable
data class PlayerLoginState(
    val settings: Map<String, String> = emptyMap(),
    val news: Map<String, String> = emptyMap(), // NewsArticle object
    val sales: List<String> = emptyList(), // assigned to sales category
    val allianceWinnings: Map<String, String> = emptyMap(),
    val recentPVPList: List<String> = emptyList(),
    val invsize: Int,
    val upgrades: String = "", // base64 encoded string
    val allianceId: String? = null,
    val allianceTag: String? = null,
    val longSession: Boolean = false, // if true: this will prompt captcha question in-game
    val leveledUp: Boolean = false,
    val promos: List<String> = emptyList(),
    val promoSale: String? = null,
    val dealItem: String? = null,
    val leaderResets: Int = 0,
    val unequipItemBinds: List<String> = emptyList(),
    val globalStats: Map<String, List<String>> = mapOf(
        "idList" to emptyList()
    ),
) {
    companion object {
        fun admin(): PlayerLoginState {
            return PlayerLoginState(
                invsize = 3000
            )
        }
    }
}
