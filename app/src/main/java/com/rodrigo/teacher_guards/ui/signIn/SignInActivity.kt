package com.rodrigo.teacher_guards.ui.signIn

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.rodrigo.teacher_guards.databinding.ActivitySigninBinding

class SignInActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySigninBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySigninBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}