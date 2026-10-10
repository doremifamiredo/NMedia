package ru.netology.nmedia.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import ru.netology.nmedia.dto.Post

class PostRepositoryInMemoryImpl : PostRepository {
    private var posts = listOf(
        Post(
            id = 1,
            author = "Нетология",
            content = "Привет! Это первый пост в нашей новой ленте 🚀",
            published = "Сегодня в 12:30",
            likes = 12,
            shares = 3,
            views = 245
        ),
        Post(
            id = 2,
            author = "Александр",
            content = "Сегодня разобрался с RecyclerView. Теперь осталось понять, куда исчезают ViewHolder при прокрутке 😄",
            published = "Сегодня в 11:15",
            likes = 125,
            shares = 18,
            views = 1520,
            likedByMe = true
        ),
        Post(
            id = 3,
            author = "Мария",
            content = "Kotlin становится всё интереснее! Особенно после Java.",
            published = "Вчера в 20:45",
            likes = 999,
            shares = 42,
            views = 9999
        ),
        Post(
            id = 4,
            author = "Android Developers",
            content = "Небольшой совет: не забывайте проверять поведение интерфейса при прокрутке длинных списков.",
            published = "Вчера в 18:20",
            likes = 1000,
            shares = 125,
            views = 15000
        ),
        Post(
            id = 5,
            author = "QA Engineer",
            content = "Нашёл баг, написал тест, исправил баг, сломал другой тест. Обычный вторник.",
            published = "8 октября в 14:10",
            likes = 2450,
            shares = 320,
            views = 125000,
            likedByMe = true
        ),
        Post(
            id = 6,
            author = "Екатерина",
            content = "Кто-нибудь знает, почему код начинает работать сразу после того, как попросишь коллегу посмотреть?",
            published = "7 октября в 09:30",
            likes = 543,
            shares = 67,
            views = 8760
        ),
        Post(
            id = 7,
            author = "Нетология",
            content = "Сегодня изучаем ListAdapter и DiffUtil. Обновляем только то, что действительно изменилось!",
            published = "6 октября в 16:00",
            likes = 10000,
            shares = 1500,
            views = 250000
        ),
        Post(
            id = 8,
            author = "Дмитрий",
            content = "Когда приложение собирается с первого раза, начинаешь подозревать, что что-то забыл.",
            published = "5 октября в 21:40",
            likes = 87,
            shares = 5,
            views = 1200,
            likedByMe = true
        ),
        Post(
            id = 9,
            author = "Mobile Testing",
            content = "Автоматизация тестирования Android — это когда Espresso знает твой интерфейс лучше тебя.",
            published = "4 октября в 13:25",
            likes = 3200,
            shares = 450,
            views = 78000
        ),
        Post(
            id = 10,
            author = "Олег",
            content = "RecyclerView работает, лайки переключаются, репосты считаются. Похоже, домашка почти готова! 🎉",
            published = "Сегодня в 14:00",
            likes = 0,
            shares = 0,
            views = 1
        )
    )
    private val data = MutableLiveData(posts)

    override fun get(): LiveData<List<Post>> = data

    override fun like(id: Long) {
        posts = posts.map { post ->
            if (post.id == id) {
                post.copy(
                    likedByMe = !post.likedByMe,
                    likes = if (post.likedByMe) post.likes - 1 else post.likes + 1
                )
            } else {
                post
            }
        }
        data.value = posts
    }

    override fun share(id: Long) {
        posts = posts.map { post ->
            if (post.id == id) {
                post.copy(
                    shares = post.shares + 1
                )
            } else {
                post
            }
        }
        data.value = posts
    }
}