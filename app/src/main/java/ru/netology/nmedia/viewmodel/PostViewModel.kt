package ru.netology.nmedia.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.dto.User
import ru.netology.nmedia.repository.PostRepository
import ru.netology.nmedia.repository.PostRepositoryInMemoryImpl
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class PostViewModel : ViewModel() {
    private val repository: PostRepository = PostRepositoryInMemoryImpl()
    val users = listOf(
        User(0L, "Ollegro"),
        User(1L, "Прима"),
        User(2L, "Секунда"),
        User(3L, "Терция"),
        User(4L, "Кварта"),
        User(5L, "Квинта"),
        User(6L, "Секста"),
        User(7L, "Септима"),
        User(8L, "Октава"),
    )
    private val _currentUser = MutableLiveData(users.first())
    val currentUser: LiveData<User> = _currentUser
    private val _edited = MutableLiveData<Post?>(null)
    val edited: LiveData<Post?> = _edited
    val data = repository.get()
    fun like(id: Long) = repository.like(id)
    fun share(id: Long) = repository.share(id)
    fun remove(post: Post) {
        if (post.authorId != currentUser.value?.id) return
        repository.remove(post.id)
        if (post.id == edited.value?.id) cancelEdit()
    }

    fun selectUser(id: Long) {
        val user = users.find { it.id == id } ?: return
        _currentUser.value = user
    }

    fun save(content: String) {
        val post = _edited.value?.copy(content = content)
            ?: Post(
                0L,
                currentUser.value?.id ?: return,
                content,
                null,
                LocalDateTime.now().format(
                    DateTimeFormatter.ofPattern("dd.MM.yyyy 'в' HH:mm")
                )
            )
        repository.save(post)
        _edited.value = null
    }

    fun edit(post: Post) {
        if (post.authorId != currentUser.value?.id) return
        _edited.value = post
    }

    fun cancelEdit() {
        _edited.value = null
    }
}
