package dev.deadzone.core.model.game.data.alliance

object AllianceRankPrivilege {
    const val None = 0
    const val ChangeLeadership = 1
    const val Disband = 2
    const val PostMessages = 4
    const val DeleteMessages = 8
    const val InviteMembers = 16
    const val RemoveMembers = 32
    const val PromoteMembers = 64
    const val DemoteMembers = 128
    const val SpendTokens = 256
    const val EditRankNames = 512
    const val EditBanner = 1024
    const val All = 1048575
}
