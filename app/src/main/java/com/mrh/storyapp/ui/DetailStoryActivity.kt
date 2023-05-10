package com.mrh.storyapp.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.mrh.storyapp.R
import com.mrh.storyapp.data.stories.StoryViewModel
import com.mrh.storyapp.databinding.ActivityDetailStoryBinding

class DetailStoryActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_NAME = "extra_name"
        const val EXTRA_PHOTOURL = "extra_photourl"
        const val EXTRA_DESCRIPTION = "extra_description"
        const val EXTRA_ID = "extra_id"
    }

        private lateinit var binding: ActivityDetailStoryBinding
        private lateinit var viewModel: StoryViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val id = intent.getStringExtra(EXTRA_ID)
        val bundle = Bundle()
        bundle.putString(EXTRA_ID, id)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        viewModel = ViewModelProvider(this)[StoryViewModel::class.java]

        if(id != null) {
            viewModel.setDetailStory(id)
        }
        showLoading(true)
        viewModel.getDetailStoryObserve().observe(this) {
            showLoading(false)
            if(it != null) {
                binding.apply {
                    tvDetailStoryName.text = it.story.name
                    tvDetailDescription.text = it.story.description
                    Glide.with(imgDetailStory)
                        .load(it.story.photoUrl)
                        .into(imgDetailStory)
                }
                supportActionBar?.title = it.story.name
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                finish()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if(isLoading) View.VISIBLE else View.GONE
    }
}