package ru.netology.service

object ChatsService {

    /**     Chat (CRUD)     */
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
    /**
     * Cписок сообщений
     * после того, как вызвана данная функция, все отданные сообщения автоматически считаются прочитанными
     */
    fun getMessages(
        idUser: Int,
        idChat: Int,
        idOffset: Int, //id последнего сообщения, начиная с которого нужно подгрузить более новые
        count: Int
    ) {

    }

    fun createMessage(idUser: Int) {

    }

    fun deleteMessage(idUser: Int) {

    }
}