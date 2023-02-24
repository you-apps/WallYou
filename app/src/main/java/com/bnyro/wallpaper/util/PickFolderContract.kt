package com.bnyro.wallpaper.util

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.DocumentsContract
import androidx.activity.result.contract.ActivityResultContract
import androidx.annotation.CallSuper

class PickFolderContract : ActivityResultContract<Uri?, Uri?>() {
    private lateinit var context: Context

    @CallSuper
    override fun createIntent(context: Context, input: Uri?): Intent {
        this.context = context

        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT_TREE).apply {
            flags = Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O && input != null) {
            intent.putExtra(DocumentsContract.EXTRA_INITIAL_URI, input)
        }
        return intent
    }

    override fun getSynchronousResult(
        context: Context,
        input: Uri?
    ): SynchronousResult<Uri?>? = null

    override fun parseResult(resultCode: Int, intent: Intent?): Uri? {
        return intent.takeIf { resultCode == Activity.RESULT_OK }?.data?.also { uri ->
            context.contentResolver.takePersistableUriPermission(
                uri,
                Intent.FLAG_GRANT_READ_URI_PERMISSION
            )
        }
    }
}
