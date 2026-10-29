package ru.netology.nmedia.activity

import android.app.Activity.RESULT_OK
import android.content.Context
import android.content.Intent
import androidx.activity.result.contract.ActivityResultContract

object PostEditorResultContract : ActivityResultContract<String?, String?>() {
    override fun createIntent(context: Context, input: String?): Intent {
        val intent = Intent(context, PostEditorActivity::class.java)
        input?.let {
            intent.putExtra(Intent.EXTRA_TEXT, it)
        }
        return intent
    }

    override fun parseResult(resultCode: Int, intent: Intent?): String? {
        return if (resultCode == RESULT_OK) {
            intent?.getStringExtra(Intent.EXTRA_TEXT)
        } else {
            null
        }
    }
}