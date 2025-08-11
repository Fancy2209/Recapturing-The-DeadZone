package dev.deadzone.socket.handler.save.misc

import dev.deadzone.context.ServerContext
import dev.deadzone.socket.handler.save.SaveSubHandler
import dev.deadzone.socket.messaging.SaveDataMethod

class MiscSaveHandler : SaveSubHandler {
    override suspend fun handle(
        type: String,
        saveId: String,
        data: Map<String, Any>,
        playerId: String,
        serverContext: ServerContext
    ) {
        when (type) {
            SaveDataMethod.TUTORIAL_PVP_PRACTICE -> {}
            SaveDataMethod.TUTORIAL_COMPLETE -> {}
            SaveDataMethod.GET_OFFERS -> {}
            SaveDataMethod.NEWS_READ -> {}
            SaveDataMethod.CLEAR_NOTIFICATIONS -> {}
            SaveDataMethod.FLUSH_PLAYER -> {}
            SaveDataMethod.SAVE_ALT_IDS -> {}
            SaveDataMethod.TRADE_DO_TRADE -> {}
            SaveDataMethod.GET_INVENTORY_SIZE -> {}
        }
    }
}
