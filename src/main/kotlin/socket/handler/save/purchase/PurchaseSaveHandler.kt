package dev.deadzone.socket.handler.save.purchase

import dev.deadzone.context.ServerContext
import dev.deadzone.socket.handler.save.SaveSubHandler
import dev.deadzone.socket.messaging.SaveDataMethod

class PurchaseSaveHandler : SaveSubHandler {
    override suspend fun handle(
        type: String,
        saveId: String,
        data: Map<String, Any>,
        playerId: String,
        serverContext: ServerContext
    ) {
        when (type) {
            SaveDataMethod.RESOURCE_BUY -> {}
            SaveDataMethod.PROTECTION_BUY -> {}
            SaveDataMethod.PAYVAULT_BUY -> {}
            SaveDataMethod.CLAIM_PROMO_CODE -> {}
            SaveDataMethod.BUY_PACKAGE -> {}
            SaveDataMethod.CHECK_APPLY_DIRECT_PURCHASE -> {}
            SaveDataMethod.HAS_PAYVAULT_ITEM -> {}
            SaveDataMethod.INCREMENT_PURCHASE_COUNT -> {}
            SaveDataMethod.DEATH_MOBILE_RENAME -> {}
        }
    }
}
