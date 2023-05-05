package com.mrh.storyapp.auth

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.mrh.storyapp.R
import com.mrh.storyapp.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {

    private lateinit var binding : ActivityLoginBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}