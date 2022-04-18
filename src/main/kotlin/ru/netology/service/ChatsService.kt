package ru.netology.service

import ru.netology.data.Chat
import ru.netology.data.Message

object ChatsService {
    private val chats = mutableListOf<Chat>()
    private var chatsCount = 1
    private var messageCount = 1

    /**     Chat (CRUD)     */
    /**create*/
    fun createChat(idUser: Int) {

    }

    /** Количество чатов, в каждом из которых есть хотя бы одно непрочитанное сообщение  */
    fun getUnreadChatsCount(idUser: Int) {

    }

    /**     Cписок чатов     */
    fun getChats(idUser: Int) {

    }

    fun deleteChat(idUser: Int) {

    }

    /**     Message (CRUD)     */
    /** create */
    fun sendMessage(
        userSenderId: Int,
        userReceiverId: Int,
        message: String
    ) {
        val newMessageId = messageCount++
        val messageToChat = Message(newMessageId, userSenderId, message)
        val chat = chats.filter { chat ->
            chat.users.containsAll(listOf(userSenderId, userReceiverId))
        }
            .firstOrNull()
            ?.let { chat ->
                chat.copy(messages = chat.messages + messageToChat)
            } ?: {
            val newChatId = chatsCount++
            Chat(
                newChatId,
                listOf(userSenderId, userReceiverId),
                listOf(messageToChat)
            )
        }
    }


    /**
     * Cписок сообщений
     * после того, как вызвана данная функция, все отданные сообщения автоматически считаются прочитанными
     */
    fun getMessages(

        idChat: Int,
        idOffset: Int, //id последнего сообщения, начиная с которого нужно подгрузить более новые
        count: Int
    ) {


    }

    fun deleteMessage(idUser: Int) {

    }
}