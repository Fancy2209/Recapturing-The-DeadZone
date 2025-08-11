package dev.deadzone.socket.handler.save.command

import dev.deadzone.context.ServerContext
import dev.deadzone.socket.handler.save.SaveSubHandler
import dev.deadzone.socket.messaging.CommandMessage
import dev.deadzone.socket.messaging.SaveDataMethod

class CommandSaveHandler : SaveSubHandler {
    override suspend fun handle(
        type: String,
        saveId: String,
        data: Map<String, Any>,
        playerId: String,
        serverContext: ServerContext
    ) {
        when (type) {
            CommandMessage.STORE_CLEAR -> {}
            CommandMessage.STORE_BLOCK -> {}
            CommandMessage.SPAWN_ELITE -> {}
            CommandMessage.ELITE_CHANCE -> {}
            CommandMessage.ADD_BOUNTY -> {}
            CommandMessage.LEVEL -> {}
            CommandMessage.SERVER_TIME -> {}
            CommandMessage.ZOMBIE -> {}
            CommandMessage.TIME -> {}
            CommandMessage.STAT -> {}
            CommandMessage.GIVE_AMOUNT -> {}
            CommandMessage.GIVE -> {}
            CommandMessage.GIVE_RARE -> {}
            CommandMessage.GIVE_UNIQUE -> {}
            CommandMessage.COUNTER -> {}
            CommandMessage.DAILY_QUEST -> {}
            CommandMessage.CHAT -> {}
            CommandMessage.LANG -> {}
            CommandMessage.FLAG -> {}
            CommandMessage.PROMO -> {}
            CommandMessage.BOUNTY_ADD -> {}
            CommandMessage.GIVE_INFECTED_BOUNTY -> {}
            CommandMessage.BOUNTY_ABANDON -> {}
            CommandMessage.BOUNTY_COMPLETE -> {}
            CommandMessage.BOUNTY_TASK_COMPLETE -> {}
            CommandMessage.BOUNTY_CONDITION_COMPLETE -> {}
            CommandMessage.BOUNTY_KILL -> {}
            CommandMessage.SKILL_GIVEXP -> {}
            CommandMessage.SKILL_LEVEL -> {}
        }
    }
}
