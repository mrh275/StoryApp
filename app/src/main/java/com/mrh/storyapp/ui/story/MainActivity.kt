package com.mrh.storyapp.ui.story

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.mrh.storyapp.R
import com.mrh.storyapp.data.stories.ListStoryItem
import com.mrh.storyapp.databinding.ActivityMainBinding
import com.mrh.storyapp.ui.auth.login.LoginActivity
import com.mrh.storyapp.ui.story.addstory.AddStoryActivity
import com.mrh.storyapp.ui.story.detail.DetailStoryActivity
import com.mrh.storyapp.ui.story.maps.StoryMapsActivity
import com.mrh.storyapp.utils.UserPreference
import com.mrh.storyapp.utils.ViewModelFactory

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val storyViewModel: StoryViewModel by viewModels {
        ViewModelFactory(this)
    }
    private lateinit var adapter: ListStoryAdapter
    private var backPressedTime: Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        showLoading(true)
        val layoutManager = LinearLayoutManager(this)
        binding.rvStories.layoutManager = layoutManager
        adapter = ListStoryAdapter()

        getData()

        adapter.setOnItemClickCallback(object : ListStoryAdapter.OnItemClickCallback {
            override fun onItemClicked(listStoryItem: ListStoryItem) {
                Intent(this@MainActivity, DetailStoryActivity::class.java).also {
                    it.putExtra(DetailStoryActivity.EXTRA_NAME, listStoryItem.name)
                    it.putExtra(DetailStoryActivity.EXTRA_DESCRIPTION, listStoryItem.description)
                    it.putExtra(DetailStoryActivity.EXTRA_PHOTOURL, listStoryItem.photoUrl)
                    it.putExtra(DetailStoryActivity.EXTRA_ID, listStoryItem.id)
                    startActivity(it)
                }
            }
        })

        binding.btnAddStory.setOnClickListener {
            val intent = Intent(this@MainActivity, AddStoryActivity::class.java)
            startActivity(intent)
        }
    }

    private fun getData() {
        showLoading(false)
        binding.rvStories.adapter = adapter.withLoadStateFooter(
            footer = LoadingStateAdapter {
                adapter.retry()
            }
        )
        storyViewModel.story.observe(this) {
            adapter.submitData(lifecycle, it)
        }
    }

    override fun onBackPressed() {
        if (backPressedTime + 3000 > System.currentTimeMillis()) {
            super.onBackPressed()
            finish()
        } else {
            Toast.makeText(
                this@MainActivity,
                "Press back again to leave the app",
                Toast.LENGTH_SHORT
            ).show()
        }
        backPressedTime = System.currentTimeMillis()
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_logout -> {
                authLogout()
            }
            R.id.showMapStory -> {
                val intent = Intent(this@MainActivity, StoryMapsActivity::class.java)
                startActivity(intent)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun authLogout() {
        UserPreference.clearAuthSession(this)
        val intent = Intent(this@MainActivity, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }
}