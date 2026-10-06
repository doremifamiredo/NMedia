package ru.netology.nmedia.activity

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import ru.netology.nmedia.R
import ru.netology.nmedia.databinding.ActivityMainBinding
import ru.netology.nmedia.formatCount
import ru.netology.nmedia.viewmodel.PostViewModel

class MainActivity : AppCompatActivity() {
    private val viewModel: PostViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val binding = ActivityMainBinding.inflate(layoutInflater)
        viewModel.data.observe(this) { post ->
            binding.author.text = post.author
            binding.published.text = post.published
            binding.content.text = post.content
            binding.likeCount.text = formatCount(post.likes)
            binding.shareCount.text = formatCount(post.shares)
            binding.like.setImageResource(
                if (post.likedByMe) R.drawable.ic_liked_24
                else R.drawable.outline_favorite_24)
            binding.viewCount.text = formatCount(post.views)
        }
        setContentView(binding.root)
        val offset = resources.getDimensionPixelSize(R.dimen.common_spacing)
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(
                systemBars.left + offset,
                systemBars.top + offset,
                systemBars.right + offset,
                systemBars.bottom + offset
            )
            insets
        }

        with(binding) {
            like.setOnClickListener {
                viewModel.like()
            }
            share.setOnClickListener {
                viewModel.share()
            }
        }
    }
}