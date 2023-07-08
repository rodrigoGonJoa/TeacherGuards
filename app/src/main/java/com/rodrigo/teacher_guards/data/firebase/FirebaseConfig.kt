package com.rodrigo.teacher_guards.data.firebase

import android.content.res.Resources
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.rodrigo.teacher_guards.R

class FirebaseConfig {
    companion object {
        fun provideFirebaseAuth(resources: Resources): GoogleSignInOptions{
            return GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(resources.getString(R.string.default_web_client_id))
                .requestEmail()
                .build()
        }
    }
}