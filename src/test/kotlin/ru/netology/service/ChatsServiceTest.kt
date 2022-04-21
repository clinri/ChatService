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
//
//    @Test
//    fun getChats() {
//        ChatsService.getChats()
//    }
//
//    @Test
//    fun deleteChat() {
//        ChatsService.deleteChat()
//    }

    @Test
    fun test_send_message() {
        Assert.assertEquals(ChatsService.sendMessage(101, 111, "тест"), 41)
    }

//    @Test
//    fun getMessages() {
//        ChatsService.getMessages()
//    }
//
//    @Test
//    fun deleteMessage() {
//        ChatsService.deleteMessage()
//    }
//
//    @Test
//    fun updateMessage() {
//        ChatsService.updateMessage()
//    }
}