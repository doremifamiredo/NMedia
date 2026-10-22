package ru.netology.nmedia.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.widget.PopupMenu
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ru.netology.nmedia.R
import ru.netology.nmedia.databinding.ItemPostBinding
import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.dto.User

class PostViewHolder(
    private val binding: ItemPostBinding,
    private val onInteractionListener: OnInteractionListener,
    private val users: List<User>,
    private val getCurrentUserId: () -> Long?
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(post: Post) {
        with(binding) {
            author.text = users.find { it.id == post.authorId }?.name ?: itemView.context.getString(
                R.string.unknown_author
            )
            content.text = post.content
            published.text = post.published
            like.text = formatCount(post.likes)
            like.isChecked = post.likedByMe
            share.text = formatCount(post.shares)
            view.text = formatCount(post.views)
            like.setOnClickListener {
                onInteractionListener.onLike(post)
            }
            share.setOnClickListener {
                onInteractionListener.onShare(post)
            }
            menu.setOnClickListener {
                PopupMenu(it.context, it).apply {
                    inflate(R.menu.post_menu)
                    val isOwner = post.authorId == getCurrentUserId()
                    menu.findItem(R.id.edit)?.isVisible = isOwner
                    menu.findItem(R.id.remove)?.isVisible = isOwner
                    menu.findItem(R.id.report)?.isVisible = !isOwner
                    setOnMenuItemClickListener { item ->
                        when (item.itemId) {
                            R.id.remove -> {
                                onInteractionListener.onRemove(post)
                                true
                            }

                            R.id.edit -> {
                                onInteractionListener.onEdit(post)
                                true
                            }

                            R.id.report -> true
                            else -> false
                        }
                    }
                }.show()
            }
        }
    }
}

class PostAdapter(
    private val onInteractionListener: OnInteractionListener,
    private val users: List<User>,
    private val getCurrentUserId: () -> Long?
) : ListAdapter<Post, PostViewHolder>(PostDiffCallback) {
    override fun onCreateViewHolder(
        parent: ViewGroup, viewType: Int
    ): PostViewHolder {
        val binding = ItemPostBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return PostViewHolder(
            binding,
            onInteractionListener,
            users,
            getCurrentUserId
        )
    }

    override fun onBindViewHolder(
        holder: PostViewHolder, position: Int
    ) {
        holder.bind(getItem(position))
    }
}

object PostDiffCallback : DiffUtil.ItemCallback<Post>() {

    override fun areItemsTheSame(oldItem: Post, newItem: Post): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Post, newItem: Post): Boolean {
        return oldItem == newItem
    }
}
