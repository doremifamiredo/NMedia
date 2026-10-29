package ru.netology.nmedia.activity

import android.content.ActivityNotFoundException
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import ru.netology.nmedia.R
import ru.netology.nmedia.adapter.OnInteractionListener
import ru.netology.nmedia.adapter.PostAdapter
import ru.netology.nmedia.databinding.ActivityMainBinding
import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.viewmodel.PostViewModel

class MainActivity : AppCompatActivity() {
    private val viewModel: PostViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val binding = ActivityMainBinding.inflate(layoutInflater)
        val postEditorLauncher =
            registerForActivityResult(PostEditorResultContract) { content ->
                if (content == null) {
                    viewModel.cancelEdit()
                    return@registerForActivityResult
                }
                viewModel.save(content)
            }
        binding.fab.setOnClickListener {
            postEditorLauncher.launch(null)
        }
        val adapter = PostAdapter(
            onInteractionListener = object : OnInteractionListener {
                override fun onLike(post: Post) = viewModel.like(post.id)
                override fun onShare(post: Post) = viewModel.share(post.id)
                override fun onRemove(post: Post) = viewModel.remove(post)
                override fun onEdit(post: Post) {
                    viewModel.edit(post)
                    postEditorLauncher.launch(post.content)
                }

                override fun onVideo(post: Post) {
                    if (post.video.isNullOrBlank()) return
                    try {
                        startActivity(Intent(Intent.ACTION_VIEW, post.video.toUri()))
                    } catch (e: ActivityNotFoundException) {
                        Toast.makeText(this@MainActivity,
                            R.string.error_open_video, Toast.LENGTH_SHORT).show()
                    }
                }
            },
            users = viewModel.users,
            getCurrentUserId = { viewModel.currentUser.value?.id }
        )
        val userAdapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item,
            viewModel.users.map { it.name }
        )
        userAdapter.setDropDownViewResource(
            android.R.layout.simple_spinner_dropdown_item
        )
        with(binding) {
            userSpinner.adapter = userAdapter
            userSpinner.onItemSelectedListener =
                object : AdapterView.OnItemSelectedListener {
                    override fun onItemSelected(
                        parent: AdapterView<*>?,
                        view: View?,
                        position: Int,
                        id: Long
                    ) {
                        val user = viewModel.users[position]
                        viewModel.selectUser(user.id)
                    }

                    override fun onNothingSelected(parent: AdapterView<*>?) {
                    }
                }
            posts.adapter = adapter
        }
        viewModel.data.observe(this) { posts ->
            adapter.submitList(posts)
        }
        viewModel.edited.observe(this) { post ->
            with(binding) {
                userSpinner.isEnabled = post == null
            }
        }
        setContentView(binding.root)
        val offset = resources.getDimensionPixelSize(R.dimen.common_spacing)
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars =
                insets.getInsets(WindowInsetsCompat.Type.systemBars() or WindowInsetsCompat.Type.ime())
            v.setPadding(
                systemBars.left + offset,
                systemBars.top + offset,
                systemBars.right + offset,
                systemBars.bottom + offset
            )
            insets
        }
    }
}