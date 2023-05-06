package com.mrh.storyapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.mrh.storyapp.auth.LoginActivity
import com.mrh.storyapp.data.UserModel
import com.mrh.storyapp.data.UserPreference
import com.mrh.storyapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding : ActivityMainBinding
    private lateinit var userModel: UserModel
    private lateinit var mUserPreference: UserPreference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        mUserPreference = UserPreference(this)
        setContentView(binding.root)

        showPreferenceData()

        binding.btnLogout.setOnClickListener{
            mUserPreference.clearAuthSession()
            val intent = Intent(this@MainActivity, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun showPreferenceData() {
        userModel = mUserPreference.getAuthSession()
        binding.sessionToken.text = userModel.token
        binding.userId.text = userModel.userId
        binding.userName.text = userModel.name
    }
}