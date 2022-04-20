package ru.netology.service

import ru.netology.data.Chat
import ru.netology.data.Message

object ChatsService {
    private var chats = mutableListOf<Chat>()
    private var chatsCount = 1
    private var messageCount = 1

    /**     Chat (CRUD)     */
    /** create не требуется, чат создается при наличии одного сообщения методом sendMessage */

    /** Количество чатов, в каждом из которых есть хотя бы одно непрочитанное сообщение  */
    fun getUnreadChatsCount(idUser: Int): Int {
        val chatsWithUnredMessages = mutableListOf<Chat>()
        chats.filter { chat -> chat.users.contains(idUser) }
            .forEachIndexed { index, chat ->
                chat.messages.forEach {
                    if (it.ownerId != idUser && it.isRead) {
                        chatsWithUnredMessages += chats[index]
                        return@forEach
                    }
                }
            }
        return chatsWithUnredMessages.size
    }

    /**     Cписок чатов     */
    fun getChats(idUser: Int): List<Chat> {
        val findedChats = chats.filter { chat -> chat.users.contains(idUser) }
        if (findedChats.isEmpty()) {
            throw NotFoundItemException("нет сообщений")
        } else {
            return findedChats
        }
    }

    /**     Message (CRUD)     */
    /** create */
    fun sendMessage(
        userSenderId: Int,
        userReceiverId: Int,
        textMessage: String
    ): Int {
        val newMessageId = messageCount++
        val message = Message(newMessageId, userSenderId, textMessage)
        if (chats.none { chat -> chat.users.containsAll(listOf(userSenderId, userReceiverId)) }) {
            val newChatId = chatsCount++
            chats += Chat(newChatId, listOf(userSenderId, userReceiverId), listOf(message))
        } else {
            chats.first { chat -> chat.users.containsAll(listOf(userSenderId, userReceiverId)) }
                .messages += message
        }
        return newMessageId
    }

    /**
     * Cписок сообщений
     * после того, как вызвана данная функция, все отданные сообщения автоматически считаются прочитанными
     */
    fun getMessages(
        idChat: Int,
        idOffset: Int = 0, //id последнего сообщения, начиная с которого нужно подгрузить более новые
        count: Int
    ): List<Message> {
        var indexChat: Int = 0
        chats.forEachIndexed { index, chat ->
            if (chat.idChat == idChat) {
                indexChat = index
                return@forEachIndexed
            }
        }
        var indexOffsetMessage: Int = 0
        if (idOffset != 0) {
            chats[indexChat].messages.forEachIndexed { index, message ->
                if (message.idMessage == idOffset) {
                    indexOffsetMessage = index
                }
            }
        }
        if (indexChat == 0 || indexOffsetMessage == 0) {
            throw NotFoundItemException("нет сообщений")
        }
        // определяем список индексов списка сообщений которые будут отмечаться прочитанными
        val foundIndexMessages = chats[indexChat].messages.subList(indexOffsetMessage, count)
            .map { message -> message.idMessage }
        for (i in foundIndexMessages) {
            if (!chats[indexChat].messages[i].isRead) {
                chats[indexChat].messages[i].isRead = true
            }
        }

        return chats[indexChat].messages.subList(indexOffsetMessage, count)
    }

    fun deleteMessage(idUser: Int) {

    }
}