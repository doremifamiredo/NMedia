package ru.netology.nmedia.dto

import kotlin.random.Random

data class Post(
    val id: Long,
    val author: String,
    val content: String,
    val published: String,
    val likes: Int = Random.nextInt(0, 999),
    val likedByMe: Boolean = false,
    val shares: Int = Random.nextInt(0, 2_000_000),
    val views: Int = Random.nextInt(0, 2_000_000)
)

