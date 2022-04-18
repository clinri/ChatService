package ru.netology.data

data class Message(
    val idMessage: Int = 0,
    val ownerId: Int,
    val message: String,
    val isRead: Boolean = false
)
