package com.mrh.storyapp.ui.auth.register

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Patterns
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.mrh.storyapp.databinding.ActivityRegisterBinding
import com.mrh.storyapp.ui.auth.login.LoginActivity
import com.mrh.storyapp.utils.ViewModelFactory

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    private lateinit var name: String
    private lateinit var email: String
    private lateinit var password: String
    private val registerViewModel: RegisterViewModel by viewModels {
        ViewModelFactory(this)
    }

    companion object {
        private const val TAG = "RegisterActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.edRegisterName.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s.isNullOrEmpty()) {
                    binding.edRegisterName.error = "Nama tidak boleh kosong"
                } else if (s.length < 3) {
                    binding.edRegisterName.error = "Nama minimal 3 karakter"
                }
            }

            override fun afterTextChanged(s: Editable?) {
            }
        })

        binding.edRegisterEmail.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s.isNullOrEmpty()) {
                    binding.edRegisterEmail.error = "Email tidak boleh kosong"
                } else if (!Patterns.EMAIL_ADDRESS.matcher(s).matches()) {
                    binding.edRegisterEmail.error = "Email tidak valid"
                }
            }

            override fun afterTextChanged(s: Editable?) {
            }
        })

        binding.btnRegister.setOnClickListener {
            name = binding.edRegisterName.text.toString()
            email = binding.edRegisterEmail.text.toString()
            password = binding.edRegisterPassword.text.toString()
            authRegister(name, email, password)
        }

        binding.btnLoginForm.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun authRegister(name: String, email: String, password: String) {

        registerViewModel.register(name, email, password).observe(this) { result ->
            if (result != null) {
                when (result) {
                    is com.mrh.storyapp.data.Result.Loading -> {
                        showLoading(true)
                    }
                    is com.mrh.storyapp.data.Result.Success -> {
                        showLoading(false)
                        if (result.data.error) {
                            Toast.makeText(this, "Registrasi gagal", Toast.LENGTH_LONG).show()
                        } else {
                            Toast.makeText(
                                this,
                                "Registrasi berhasil, silahkan login!",
                                Toast.LENGTH_LONG
                            ).show()
                            startActivity(Intent(this@RegisterActivity, LoginActivity::class.java))
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
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
}

