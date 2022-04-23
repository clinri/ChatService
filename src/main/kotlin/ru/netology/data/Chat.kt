package ru.netology.data

data class Chat(
    val idChat: Int,
    val users: List<Int>,
    var messages: List<Message>
)