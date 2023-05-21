package com.mrh.storyapp.ui.story.detail

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.activity.viewModels
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.mrh.storyapp.ui.story.StoryViewModel
import com.mrh.storyapp.databinding.ActivityDetailStoryBinding
import com.mrh.storyapp.utils.ViewModelFactory

class DetailStoryActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_NAME = "extra_name"
        const val EXTRA_PHOTOURL = "extra_photourl"
        const val EXTRA_DESCRIPTION = "extra_description"
        const val EXTRA_ID = "extra_id"
    }

        private lateinit var binding: ActivityDetailStoryBinding
        private lateinit var viewModel: StoryViewModel
        private val storyViewModel: StoryViewModel by viewModels {
            ViewModelFactory(this)
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        playAnimation()

        val id = intent.getStringExtra(EXTRA_ID)
        val bundle = Bundle()
        bundle.putString(EXTRA_ID, id)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        viewModel = ViewModelProvider(this)[StoryViewModel::class.java]

        if(id != null) {
            showLoading(true)
            storyViewModel.detailStory(id, this)
        }
        viewModel.getDetailStoryObserve().observe(this) {
            showLoading(false)
            if(it != null) {
                binding.apply {
                    tvDetailName.text = it.story.name
                    tvDetailDescription.text = it.story.description
                    Glide.with(ivDetailPhoto)
                        .load(it.story.photoUrl)
                        .into(ivDetailPhoto)
                }
                supportActionBar?.title = it.story.name
            }
        }
    }

    private fun playAnimation() {
        val imageStory = ObjectAnimator.ofFloat(binding.ivDetailPhoto, View.TRANSLATION_X, 1000f, 0f).setDuration(300)
        val ownerStory = ObjectAnimator.ofFloat(binding.tvDetailName, View.TRANSLATION_X, 1000f, 0f).setDuration(300)
        val descStory = ObjectAnimator.ofFloat(binding.tvDetailDescription, View.TRANSLATION_X, 1000f, 0f).setDuration(300)

        AnimatorSet().apply {
            playSequentially(imageStory, ownerStory, descStory)
            start()
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