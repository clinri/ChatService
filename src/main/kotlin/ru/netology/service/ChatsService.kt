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
        var chatsWithUnreadMessages = 0
        chats.filter { it.users.contains(idUser) }
            .forEach { chat ->
                chat.messages.forEach forEachMessages@{
                    //отображать непрочитанными будем чаты если пользователь получатель
                    if (it.ownerId != idUser && !it.isRead) {
                        chatsWithUnreadMessages++
                        return@forEachMessages
                    }
                }
            }
        return chatsWithUnreadMessages
    }

    /** read Cписок чатов     */
    fun getChats(idUser: Int): List<Chat> {
        val foundChats = chats.filter { it.users.contains(idUser) }
        if (foundChats.isEmpty()) {
            throw NotFoundItemException("нет сообщений")
        } else {
            return foundChats
        }
    }

    /** delete     */
    fun deleteChat(idUser: Int, idChat: Int): Boolean {
        chats.forEachIndexed { index, chat ->
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
        val newMessageId = messageCount++
        val message = Message(newMessageId, userSenderId, textMessage)
        if (chats.none { it.users.containsAll(listOf(userSenderId, userReceiverId)) }) {
            val newChatId = chatsCount++
            chats += Chat(newChatId, listOf(userSenderId, userReceiverId), listOf(message))
        } else {
            chats.first { it.users.containsAll(listOf(userSenderId, userReceiverId)) }
                .messages += message
        }
        return newMessageId
    }

    /** read
     * Cписок сообщений после того, как вызвана данная функция,
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
        chats.forEachIndexed { index, chat ->
            if (chat.idChat == idChat && chat.users.contains(idUser)) {
                indexChat = index
                return@forEachIndexed
            }
        }
        if (indexChat == null) {
            throw NotFoundItemException("нет сообщений")
        }
        //индекс сообщения начиная с которого нужно подгрузить более новые
        var indexOffsetMessage: Int = 0
        if (idOffset != 0) {
            chats[indexChat!!].messages.forEachIndexed { index, message ->
                if (message.idMessage == idOffset) {
                    indexOffsetMessage = index
                }
            }
        }
        // определяем список индексов списка сообщений которые будут отмечаться прочитанными
        val foundIndexMessages = chats[indexChat!!].messages.subList(indexOffsetMessage, count)
            // будем отмечать прочитанными только если сообщения адресованы пользователю
            .filter { it.ownerId != idUser }
            .map { it.idMessage }
        for (i in foundIndexMessages) {
            if (!chats[indexChat!!].messages[i].isRead) {
                chats[indexChat!!].messages[i].isRead = true
            }
        }
        return chats[indexChat!!].messages.subList(indexOffsetMessage, count)
    }

    /**     delete     */
    fun deleteMessage(idUser: Int, idMessage: Int): Boolean {
        var indexChat: Int? = null
        var sizeMessage: Int = 0
        // индекс чата
        chats.forEachIndexed { index, chat ->
            if (chat.messages.any { message -> message.idMessage == idMessage && chat.users.contains(idUser) }) {
                indexChat = index
            }
            if (indexChat != null) {
                sizeMessage = chat.messages.size
            }
        }
        return when (sizeMessage) {
            0 -> false
            1 -> {
                chats.removeAt(indexChat!!)
                true
            }
            else -> {
                val messagesEdit = chats[indexChat!!].messages.filter { it.idMessage != idMessage }
                chats[indexChat!!] = chats[indexChat!!].copy(messages = messagesEdit)
                true
            }
        }
    }

    /**     update     */
    fun updateMessage(idUser: Int, idMessage: Int, textMessage: String): Message {
        var indexChat: Int? = null
        chats.forEachIndexed { index, chat ->
            if (chat.messages.any { it.idMessage == idMessage && chat.users.contains(idUser) }) {
                indexChat = index
            }
        }
        if (indexChat == null) {
            throw NotFoundItemException("нет такого сообщения")
        }
        val messagesEdit = chats[indexChat!!].messages
        var messageIndex: Int? = null
        messagesEdit.forEachIndexed { index, message ->
            // редактируем только свои сообщения
            if (message.idMessage == idMessage && message.ownerId == idUser) {
                messageIndex = index
            }
        }
        if (messageIndex == null) {
            throw NotFoundItemException("нет такого сообщения")
        }
        messagesEdit[messageIndex!!].message = textMessage
        chats[indexChat!!] = chats[indexChat!!].copy(messages = messagesEdit)
        return messagesEdit[messageIndex!!]
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

fun main() {
    ChatsService.createChats()
    ChatsService.getMessages(1, 1, 2, 1).forEach { println(it.message) }
}