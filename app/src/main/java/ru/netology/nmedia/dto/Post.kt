package ru.netology.nmedia.dto

data class Post(
    val id: Long,
    val authorId: Long,
    val content: String,
    val published: String,
    val likes: Int = 0,
    val likedByMe: Boolean = false,
    val shares: Int = 0,
    val views: Int = 0
)

