package com.mrh.storyapp.ui.auth.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Patterns
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import com.mrh.storyapp.ui.story.MainActivity
import com.mrh.storyapp.ui.auth.register.RegisterActivity
import com.mrh.storyapp.utils.UserPreference
import com.mrh.storyapp.databinding.ActivityLoginBinding
import com.mrh.storyapp.utils.ViewModelFactory

class LoginActivity : AppCompatActivity() {

    private lateinit var binding : ActivityLoginBinding
    private lateinit var email: String
    private lateinit var password: String
    private var backPressedTime: Long = 0
    private val loginViewModel: LoginViewModel by viewModels {
        ViewModelFactory(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        checkSession()
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.edLoginEmail.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if(s.isNullOrEmpty()) {
                    binding.edLoginEmail.error = "Email tidak boleh kosong"
                } else if(!Patterns.EMAIL_ADDRESS.matcher(s).matches()) {
                    binding.edLoginEmail.error = "Email tidak valid"
                }
            }

            override fun afterTextChanged(s: Editable?) {
            }

        })

        binding.btnLogin.setOnClickListener {
            email = binding.edLoginEmail.text.toString()
            password = binding.edLoginPassword.text.toString()
            authLogin(email, password)
        }

        binding.btnRegisterForm.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onBackPressed() {
        if(backPressedTime + 3000 > System.currentTimeMillis()) {
            super.onBackPressed()
            finishAffinity()
        } else {
            Toast.makeText(this@LoginActivity, "Press back again to leave the app", Toast.LENGTH_SHORT).show()
        }
        backPressedTime = System.currentTimeMillis()
    }

    private fun authLogin(email: String, password: String) {
        loginViewModel.login(email, password).observe(this) { result ->
            if(result != null) {
                when(result) {
                    is com.mrh.storyapp.data.Result.Loading -> {
                        showLoading(true)
                    }
                    is com.mrh.storyapp.data.Result.Success -> {
                        showLoading(false)
                        if(result.data.error) {
                            Toast.makeText(this, result.data.message, Toast.LENGTH_LONG).show()
                        } else {
                            UserPreference.setAuthSession(result.data.loginResult.token, this)
                            startActivity(Intent(this@LoginActivity, MainActivity::class.java))
                            finish()
                        }
                    }
                    is com.mrh.storyapp.data.Result.Error -> {
                        showLoading(false)
                        Toast.makeText(this, result.error, Toast.LENGTH_LONG).show()
                    }
                }
            }
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if(isLoading) View.VISIBLE else View.GONE
    }

    private fun checkSession() {
        val token = UserPreference.getAuthSession(this)
        if(token.isNotBlank()) {
            val intent = Intent(this@LoginActivity, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}