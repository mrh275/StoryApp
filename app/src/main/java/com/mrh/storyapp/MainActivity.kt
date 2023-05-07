package com.mrh.storyapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.RecyclerView
import com.mrh.storyapp.auth.LoginActivity
import com.mrh.storyapp.data.UserModel
import com.mrh.storyapp.data.UserPreference
import com.mrh.storyapp.data.stories.ListStoryAdapter
import com.mrh.storyapp.data.stories.ListStoryItem
import com.mrh.storyapp.data.stories.StoryViewModel
import com.mrh.storyapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding : ActivityMainBinding
    private lateinit var userModel: UserModel
    private lateinit var mUserPreference: UserPreference
    private lateinit var rvStories: RecyclerView
    private lateinit var storyViewModel: StoryViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        mUserPreference = UserPreference(this)
        setContentView(binding.root)
        storyViewModel = StoryViewModel()

        rvStories = binding.rvStories
        rvStories.setHasFixedSize(true)
        storyViewModel.listStory.observe(this) { listStory ->
            getAllStories(listStory)
        }

        binding.btnLogout.setOnClickListener{
            mUserPreference.clearAuthSession()
            val intent = Intent(this@MainActivity, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun getAllStories(listStoryItem: List<ListStoryItem>) {
        val listStory = listStoryItem.map {
            "${it.photoUrl}\n- ${it.name}"
        }
        val adapter = ListStoryAdapter(listStory)
        binding.rvStories.adapter = adapter
    }
}