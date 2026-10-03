package ru.netology.nmedia.dto

import kotlin.random.Random

data class Post(
    val id: Long,
    val author: String,
    val content: String,
    val published: String,
    var likes: Int = Random.nextInt(0, 2_000_000),
    var likedByMe: Boolean = false,
    var shares: Int = Random.nextInt(0, 2_000_000),
    var views: Int = Random.nextInt(0, 2_000_000)
)

