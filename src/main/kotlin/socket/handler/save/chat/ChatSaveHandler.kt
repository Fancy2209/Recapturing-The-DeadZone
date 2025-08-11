package dev.deadzone.socket.handler.save.chat

import dev.deadzone.context.ServerContext
import dev.deadzone.socket.handler.save.SaveSubHandler
import dev.deadzone.socket.messaging.SaveDataMethod

class ChatSaveHandler : SaveSubHandler {
    override suspend fun handle(
        type: String,
        saveId: String,
        data: Map<String, Any>,
        playerId: String,
        serverContext: ServerContext
    ) {
        when (type) {
            SaveDataMethod.CHAT_SILENCED -> {}
            SaveDataMethod.CHAT_KICKED -> {}
            SaveDataMethod.CHAT_GET_CONTACTS_AND_BLOCKS -> {}
            SaveDataMethod.CHAT_MIGRATE_CONTACTS_AND_BLOCKS -> {}
            SaveDataMethod.CHAT_ADD_CONTACT -> {}
            SaveDataMethod.CHAT_REMOVE_CONTACT -> {}
            SaveDataMethod.CHAT_REMOVE_ALL_CONTACTS -> {}
            SaveDataMethod.CHAT_ADD_BLOCK -> {}
            SaveDataMethod.CHAT_REMOVE_BLOCK -> {}
            SaveDataMethod.CHAT_REMOVE_ALL_BLOCKS -> {}
        }
    }
}
