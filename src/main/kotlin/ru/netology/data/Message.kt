package ru.netology.data

data class Message(
    val idMessage: Int = 0,
    val ownerId: Int,
    var message: String,
    var isRead: Boolean = false
)
