package dev.deadzone.socket.handler.save.item

import dev.deadzone.context.ServerContext
import dev.deadzone.socket.handler.save.SaveSubHandler
import dev.deadzone.socket.messaging.SaveDataMethod

class ItemSaveHandler : SaveSubHandler {
    override suspend fun handle(
        type: String,
        saveId: String,
        data: Map<String, Any>,
        playerId: String,
        serverContext: ServerContext
    ) {
        when (type) {
            SaveDataMethod.ITEM -> {}
            SaveDataMethod.ITEM_BUY -> {}
            SaveDataMethod.ITEM_LIST -> {}
            SaveDataMethod.ITEM_RECYCLE -> {}
            SaveDataMethod.ITEM_DISPOSE -> {}
            SaveDataMethod.ITEM_CLEAR_NEW -> {}
            SaveDataMethod.ITEM_BATCH_RECYCLE -> {}
            SaveDataMethod.ITEM_BATCH_RECYCLE_SPEED_UP -> {}
            SaveDataMethod.ITEM_BATCH_DISPOSE -> {}
        }
    }
}
