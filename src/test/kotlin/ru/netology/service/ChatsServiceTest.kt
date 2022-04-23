package ru.netology.service

import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test

class ChatsServiceTest {
    @Before
    fun createChats() {
        ChatsService.createChats()
    }

    @After
    fun removeAll() {
        ChatsService.removeAll()
    }

    @Test
    fun test_get_unread_chats_count() {
        Assert.assertEquals(ChatsService.getUnreadChatsCount(21), 5)
    }

    @Test
    fun get_chats_whith_messages() {
        Assert.assertTrue(ChatsService.getChats(1).isNotEmpty())
    }

    @Test(expected = NotFoundItemException::class)
    fun get_chats_if_not_messages() {
        ChatsService.getChats(200)
    }

    @Test
    fun delete_chat() {
        Assert.assertTrue(ChatsService.deleteChat(1, 1))
    }

    @Test
    fun test_send_message() {
        Assert.assertEquals(ChatsService.sendMessage(101, 111, "тест"), 101)
    }

    @Test
    fun get_messages() {
        val list = ChatsService.getMessages(1, 1, 0, 1)
        Assert.assertTrue(list.isNotEmpty())
    }

    @Test
    fun delete_message() {
        Assert.assertTrue(ChatsService.deleteMessage(1, 1))
    }

    @Test
    fun update_message() {
        Assert.assertEquals(ChatsService.updateMessage(1, 1, "Новое сообщение").message,
            "Новое сообщение")
    }
}