package ru.netology.nmedia.adapter

import ru.netology.nmedia.dto.Post

interface OnLikeListener {
    fun onLike(post: Post)
}

interface OnShareListener {
    fun onShare(post: Post)
}
