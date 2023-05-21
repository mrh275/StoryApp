package com.mrh.storyapp.ui.story.maps

import androidx.lifecycle.ViewModel
import com.mrh.storyapp.data.stories.StoryRepository

class StoryMapsViewModel(private val storyRepository: StoryRepository) : ViewModel() {
    fun getStoryWithLocation() = storyRepository.getStoryWithLocation()
}