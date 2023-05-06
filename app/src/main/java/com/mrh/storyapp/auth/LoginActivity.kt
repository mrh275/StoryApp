package com.mrh.storyapp.auth

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.mrh.storyapp.MainActivity
import com.mrh.storyapp.api.ApiConfig
import com.mrh.storyapp.api.ResponseLogin
import com.mrh.storyapp.data.UserModel
import com.mrh.storyapp.data.UserPreference
import com.mrh.storyapp.databinding.ActivityLoginBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity() {

    private lateinit var binding : ActivityLoginBinding
    private lateinit var email: String
    private lateinit var password: String
    private lateinit var token: String
    private lateinit var userId: String
    private lateinit var name: String
    private lateinit var userModel: UserModel

    companion object {
        private const val TAG = "LoginActivity"
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        userModel = UserModel()

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

    private fun authLogin(email: String, password: String) {
        showLoading(true)
        val client = ApiConfig.getApiService().login(email, password)
        client.enqueue(object : Callback<ResponseLogin> {
            override fun onResponse(call: Call<ResponseLogin>, response: Response<ResponseLogin>) {
                if(response.isSuccessful) {
                    showLoading(false)
                    token = response.body()?.loginResult?.token.toString()
                    userId = response.body()?.loginResult?.userId.toString()
                    name = response.body()?.loginResult?.name.toString()
                    storeAuthSession(token, userId, name)
                    Toast.makeText(this@LoginActivity, "Welcome back ${response.body()?.loginResult?.name}", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this@LoginActivity, MainActivity::class.java)
                    startActivity(intent)
                } else {
                    showLoading(false)
                    Toast.makeText(this@LoginActivity, "Email atau Kata Sandi salah", Toast.LENGTH_SHORT).show()
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<ResponseLogin>, t: Throwable) {
                showLoading(false)
                Toast.makeText(this@LoginActivity, "Gagal terkoneksi ke server", Toast.LENGTH_SHORT).show()
                Log.e(TAG, "onFailure: ${t.message}")
            }
        })
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if(isLoading) View.VISIBLE else View.GONE
    }

    private fun storeAuthSession(token: String, userId: String, name: String) {
        val userPreference = UserPreference(this)

        userModel.token = token
        userModel.userId = userId
        userModel.name = name

        userPreference.setAuthSession(userModel)
    }
}