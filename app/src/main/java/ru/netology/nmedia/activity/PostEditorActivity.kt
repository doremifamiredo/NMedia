package ru.netology.nmedia.activity

import android.content.Intent
import android.content.Intent.EXTRA_TEXT
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import ru.netology.nmedia.R
import ru.netology.nmedia.databinding.ActivityPostEditorBinding

class PostEditorActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityPostEditorBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val bars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            val bottomInset = insets.getInsets(
                WindowInsetsCompat.Type.systemBars() or WindowInsetsCompat.Type.ime()
            ).bottom

            v.setPadding(bars.left, bars.top, bars.right, bottomInset)
            insets
        }
        intent.getStringExtra(EXTRA_TEXT)?.let {
            binding.postContent.setText(it)
        }
        binding.save.setOnClickListener {
            val content = binding.postContent.text.toString().trim()
            if (content.isBlank()) {
                binding.postContent.error = getString(R.string.error_empty_post)
                return@setOnClickListener
            }
            val resultIntent = Intent()
            resultIntent.putExtra(EXTRA_TEXT, content)
            setResult(RESULT_OK, resultIntent)
            finish()
        }
    }
}