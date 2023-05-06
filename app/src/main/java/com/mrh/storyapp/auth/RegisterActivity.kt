package com.mrh.storyapp.auth

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.mrh.storyapp.MainActivity
import com.mrh.storyapp.api.ApiConfig
import com.mrh.storyapp.api.ResponseRegister
import com.mrh.storyapp.databinding.ActivityRegisterBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    private lateinit var name: String
    private lateinit var email: String
    private lateinit var password: String

    companion object {
        private const val TAG = "RegisterActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnRegister.setOnClickListener {
            name = binding.edRegisterName.text.toString()
            email = binding.edRegisterEmail.text.toString()
            password = binding.edRegisterPassword.text.toString()
            authRegister(name, email, password)
        }

        binding.btnLoginForm.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
    }

    private fun authRegister(name: String, email: String, password: String) {
        showLoading(true)
        val client = ApiConfig.getApiService().register(name, email, password)
        client.enqueue(object : Callback<ResponseRegister> {
            override fun onResponse(call: Call<ResponseRegister>, response: Response<ResponseRegister>) {
                if(response.isSuccessful) {
                    showLoading(false)
                    Toast.makeText(this@RegisterActivity, "Welcome $name", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this@RegisterActivity, MainActivity::class.java)
                    startActivity(intent)
                } else {
                    showLoading(false)
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<ResponseRegister>, t: Throwable) {
                showLoading(false)
                Log.e(TAG, "onFailure: ${t.message}")
            }
        })
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if(isLoading) View.VISIBLE else View.GONE
    }
}

