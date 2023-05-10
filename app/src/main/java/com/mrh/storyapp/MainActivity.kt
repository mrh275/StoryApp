package com.mrh.storyapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.mrh.storyapp.auth.LoginActivity
import com.mrh.storyapp.data.UserPreference
import com.mrh.storyapp.data.stories.ListStoryAdapter
import com.mrh.storyapp.data.stories.ListStoryItem
import com.mrh.storyapp.data.stories.StoryViewModel
import com.mrh.storyapp.databinding.ActivityMainBinding
import com.mrh.storyapp.ui.DetailStoryActivity

class MainActivity : AppCompatActivity() {

    private lateinit var binding : ActivityMainBinding
    private lateinit var mUserPreference: UserPreference
    private val storyViewModel by viewModels<StoryViewModel>()
    private lateinit var adapter: ListStoryAdapter
    private var backPressedTime: Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        mUserPreference = UserPreference(this)
        setContentView(binding.root)

        showLoading(true)
        val layoutManager = LinearLayoutManager(this)
        binding.rvStories.layoutManager = layoutManager
        adapter = ListStoryAdapter(this)
        binding.rvStories.adapter = adapter
        binding.rvStories.setHasFixedSize(true)
        val itemDecoration = DividerItemDecoration(this, layoutManager.orientation)
        binding.rvStories.addItemDecoration(itemDecoration)

        storyViewModel.getListStoryObserve().observe(this) { listStory ->
            showLoading(false)
            if(listStory != null) {
                adapter.setListStory(listStory)
                adapter.notifyDataSetChanged()
            } else {
                Toast.makeText(this, "Error fetching data", Toast.LENGTH_SHORT).show()
            }
        }
        storyViewModel.findStories()

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
    }

    override fun onBackPressed() {
        if(backPressedTime + 3000 > System.currentTimeMillis()) {
            super.onBackPressed()
            finishAffinity()
        } else {
            Toast.makeText(this@MainActivity, "Press back again to leave the app", Toast.LENGTH_SHORT).show()
        }
        backPressedTime = System.currentTimeMillis()
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if(isLoading) View.VISIBLE else View.GONE
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.logout -> {
                authLogout()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun authLogout() {
        mUserPreference.clearAuthSession()
        val intent = Intent(this@MainActivity, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }
}