package ru.netology.service

import ru.netology.data.Chat
import ru.netology.data.Message

object ChatsService {
    private var chats = mutableListOf<Chat>()
    private var chatsCount = 1
    private var messageCount = 1

    /**     Chat (CRUD)     */
    /** create не требуется, чат создается при наличии одного сообщения методом sendMessage */
    /** update не требуется */
    /** Количество чатов, в каждом из которых есть хотя бы одно непрочитанное сообщение  */
    fun getUnreadChatsCount(idUser: Int): Int {
        var result = 0
        chats.asSequence()
            .filter { it.users.contains(idUser) }
            .forEach { chat ->
                if (chat.messages.any { it.ownerId != idUser && !it.isRead }) result++
            }
        return result
    }

    /** read Cписок чатов     */
    fun getChats(idUser: Int): List<Chat> {
        return chats.asSequence()
            .filter { it.users.contains(idUser) }
            .ifEmpty { throw NotFoundItemException("нет сообщений") }
            .toList()
    }

    /** delete     */
    fun deleteChat(idUser: Int, idChat: Int): Boolean {
        chats.asSequence()
            .forEachIndexed { index, chat ->
                if (chat.users.contains(idUser) && chat.idChat == idChat) {
                    chats.removeAt(index)
                    return true
                }
            }
        return false
    }

    /**     Message (CRUD)     */
    /** create */
    fun sendMessage(
        userSenderId: Int,
        userReceiverId: Int,
        textMessage: String
    ): Int {
        val newMessage = Message(messageCount++, userSenderId, textMessage)
        if (chats.none { it.users.containsAll(listOf(userSenderId, userReceiverId)) }) {
            chats + Chat(chatsCount++, listOf(userSenderId, userReceiverId), listOf(newMessage))
        } else {
            chats.first { it.users.containsAll(listOf(userSenderId, userReceiverId)) }
                .messages + newMessage
        }
        return newMessage.idMessage
    }

    /** read
     * Cписок сообщений после того как вызвана данная функция,
     * все отданные сообщения автоматически считаются прочитанными
     */
    fun getMessages(
        idUser: Int,
        idChat: Int,
        idOffset: Int = 0, //id последнего сообщения, начиная с которого нужно подгрузить более новые
        count: Int
    ): List<Message> {
        var indexChat: Int? = null
        //индекс чата
        chats.asSequence()
            .forEachIndexed { index, chat ->
                if (chat.idChat == idChat && chat.users.contains(idUser)) {
                    indexChat = index
                    return@forEachIndexed
                }
            }
        indexChat?.let { chatIdx ->
            //индекс сообщения начиная с которого нужно подгрузить более новые
            var indexOffsetMessage: Int = 0
            if (idOffset != 0) {
                chats[chatIdx].messages.forEachIndexed { index, message ->
                    if (message.idMessage == idOffset) {
                        indexOffsetMessage = index
                    }
                }
            }
            // определяем список индексов списка сообщений которые будут отмечаться прочитанными
            val foundIndexMessages = chats[chatIdx].messages
                .subList(indexOffsetMessage, count)
                // будем отмечать прочитанными только если сообщения адресованы пользователю
                .filter { it.ownerId != idUser }
                .map { it.idMessage }
            for (i in foundIndexMessages) {
                if (!chats[chatIdx].messages[i].isRead) {
                    chats[chatIdx].messages[i].isRead = true
                }
            }
            return chats[chatIdx].messages.subList(indexOffsetMessage, count)
        } ?: throw NotFoundItemException("нет сообщений")
    }


    /**     delete     */
    fun deleteMessage(idUser: Int, idMessage: Int): Boolean {
        var indexChat: Int? = null
        var sizeMessage: Int = 0
        // индекс чата

        chats.forEachIndexed { index, chat ->
            if (chat.messages.any { msg -> msg.idMessage == idMessage && chat.users.contains(idUser) }) {
                indexChat = index
            }
            indexChat?.let { sizeMessage = chat.messages.size } ?: return false
        }
        var result: Boolean? = null
        indexChat?.let { chatIdx ->
            when (sizeMessage) {
                0 -> result = false
                1 -> {
                    chats.removeAt(chatIdx)
                    result = true
                }
                else -> {
                    val messagesEdit = chats[chatIdx].messages.filter { value -> value.idMessage != idMessage }
                    chats[chatIdx] = chats[chatIdx].copy(messages = messagesEdit)
                    result = true
                }
            }
        } ?: return false
        result?.let { return it } ?: return false
    }

    /**     update     */
    fun updateMessage(idUser: Int, idMessage: Int, textMessage: String): Message {
        var indexChat: Int? = null
        chats.asSequence()
            .forEachIndexed { index, chat ->
                if (chat.messages.any { it.idMessage == idMessage && chat.users.contains(idUser) }) {
                    indexChat = index
                }
            }
        indexChat?.let { chatIdx ->
            val messagesEdit = chats[chatIdx].messages
            var messageIndex: Int? = null
            messagesEdit.forEachIndexed { index, message ->
                // редактируем только свои сообщения
                if (message.idMessage == idMessage && message.ownerId == idUser) {
                    messageIndex = index
                }
            }
            messageIndex?.let { msgIdx ->
                messagesEdit[msgIdx].message = textMessage
                chats[chatIdx] = chats[chatIdx].copy(messages = messagesEdit)
                return messagesEdit[msgIdx]
            } ?: throw NotFoundItemException("нет такого сообщения")
        } ?: throw NotFoundItemException("нет такого сообщения")
    }

    fun removeAll() {
        chats.clear()
        chatsCount = 1
        messageCount = 1
    }

    fun createChats() {
        for (k in 1..5) {
            for (i in 1..10) {
                sendMessage(k, 20 + i, "Сообщение от $k про $k")
                sendMessage(20 + i, k, "Сообщение от ${20 + i} про $k")
            }
        }
        println("Сообщения отправлены")
    }
}