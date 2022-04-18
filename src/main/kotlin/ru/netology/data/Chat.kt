package ru.netology.data

data class Chat(
    val idChat: Int,
    val users: List<Int>,
    val messages: List<Message>
)