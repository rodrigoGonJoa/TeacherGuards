package com.rodrigo.teacher_guards.ui.signIn

import android.content.Intent
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher

interface ActivityLauncher {
    fun registerActivityResult(onResult: (ActivityResult) -> Unit): ActivityResultLauncher<Intent>
    fun launchActivityForResult(launcher: ActivityResultLauncher<Intent>, intent: Intent)
}
