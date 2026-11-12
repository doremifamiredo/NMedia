package ru.netology.nmedia.repository

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import ru.netology.nmedia.dto.Post
import com.google.gson.reflect.TypeToken

class PostRepositoryFileImpl(
    private val context: Context
) : PostRepository {
    private val file = context.filesDir.resolve("posts.json")
    private val gson = Gson()
    private val postsType = object : TypeToken<List<Post>>() {}.type
    private var posts = listOf(
        Post(
            id = 1,
            authorId = 0,
            content = "Олег и Фречча",
            video = "https://rutube.ru/video/private/c2d9c65177e986ac66f62ec0a5624227/?p=Slg9XlxKdWTkcxWJd_jl1g",
            published = "22.12.1991",
            likes = 24,
            shares = 999,
            views = 2020,
            likedByMe = false
        ),
        Post(
            id = 2,
            authorId = 6,
            content = "Говорят, я звучу романтично. А я просто стараюсь поддерживать гармоничные отношения.",
            published = "Сегодня в 11:15",
            likes = 125,
            shares = 18,
            views = 1520,
            likedByMe = true
        ),
        Post(
            id = 3,
            authorId = 3,
            content = "Опять все спорят, мажор я или минор. А можно мне просто побыть собой?.",
            published = "Вчера в 20:45",
            likes = 999,
            shares = 42,
            views = 9999
        ),
        Post(
            id = 4,
            authorId = 7,
            content = "Психолог сказал, что мне нужно научиться отпускать напряжение. Кажется, он намекает на тонику.",
            published = "Вчера в 18:20",
            likes = 1000,
            shares = 125,
            views = 15000
        ),
        Post(
            id = 5,
            authorId = 2,
            content = "Соседи жалуются, что мы слишком близко друг к другу живём. Особенно когда начинаем звучать одновременно.",
            published = "8 октября в 14:10",
            likes = 2450,
            shares = 320,
            views = 125000,
            likedByMe = true
        ),
        Post(
            id = 6,
            authorId = 1,
            content = "Не понимаю, зачем искать вторую половинку, если можно прекрасно звучать в унисон с собой.",
            published = "7 октября в 09:30",
            likes = 543,
            shares = 67,
            views = 8760
        ),
        Post(
            id = 7,
            authorId = 4,
            content = "Все восхищаются Квинтой, а кто-нибудь вообще помнит, что я тоже чистая?",
            published = "6 октября в 16:00",
            likes = 10000,
            shares = 1500,
            views = 250000
        ),
        Post(
            id = 8,
            authorId = 8,
            content = "Сегодня встретила человека, с которым мы абсолютно одинаковые. Только он на восемь ступеней ниже.",
            published = "5 октября в 21:40",
            likes = 87,
            shares = 5,
            views = 1200,
            likedByMe = true
        ),
        Post(
            id = 9,
            authorId = 5,
            content = "Стабильность — моё второе имя. Если нужен надёжный фундамент для гармонии, обращайтесь.",
            published = "4 октября в 13:25",
            likes = 3200,
            shares = 450,
            views = 78000
        ),
        Post(
            id = 10,
            authorId = 4,
            content = "Меня сегодня назвали тритоном. Всего полтона разницы, а репутация уже испорчена!",
            published = "Сегодня в 14:00",
            likes = 0,
            shares = 0,
            views = 1
        ),
        Post(
            id = 11,
            authorId = 7,
            content = "Не люблю недосказанность. Всегда оставляю ощущение, что вот-вот что-то разрешится...",
            published = "Сегодня в 12:30",
            likes = 12,
            shares = 3,
            views = 245
        ),
    )
    private val data = MutableLiveData(posts)
    private var nextId = 12L

    init {
        if (file.exists()) {
            val json = file.readText()
            posts = gson.fromJson(json, postsType)
            data.value = posts
        } else {
            sync()
        }
        nextId = (posts.maxOfOrNull { it.id } ?: 0L) + 1
    }

    override fun get(): LiveData<List<Post>> = data

    override fun like(id: Long) {
        updatePosts(posts.map { post ->
            if (post.id == id) {
                post.copy(
                    likedByMe = !post.likedByMe,
                    likes = if (post.likedByMe) post.likes - 1 else post.likes + 1
                )
            } else {
                post
            }
        })
    }

    override fun share(id: Long) {
        updatePosts(posts.map { post ->
            if (post.id == id) {
                post.copy(
                    shares = post.shares + 1
                )
            } else {
                post
            }
        })
    }

    override fun remove(id: Long) {
        updatePosts(posts.filter { it.id != id })
    }

    override fun save(post: Post) {
        val updatedPosts = if (post.id == 0L) {
            listOf(post.copy(id = nextId++)) + posts
        } else {
            posts.map {
                if (it.id == post.id) {
                    it.copy(content = post.content)
                } else {
                    it
                }
            }
        }

        updatePosts(updatedPosts)
    }

    private fun updatePosts(newPosts: List<Post>) {
        posts = newPosts
        sync()
        data.value = posts
    }

    private fun sync() {
        val json = gson.toJson(posts)
        file.writeText(json)
    }
}