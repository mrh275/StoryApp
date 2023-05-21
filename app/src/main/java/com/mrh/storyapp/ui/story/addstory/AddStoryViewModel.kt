package com.mrh.storyapp.ui.story.addstory

import androidx.lifecycle.ViewModel
import com.mrh.storyapp.data.stories.StoryRepository
import okhttp3.MultipartBody
import okhttp3.RequestBody

class AddStoryViewModel(private val storyRepository: StoryRepository) : ViewModel() {
    fun addNewStory(imageMultiPart: MultipartBody.Part, description: RequestBody, lat: Double, lon: Double) = storyRepository.addStory(imageMultiPart, description, lat, lon)

}