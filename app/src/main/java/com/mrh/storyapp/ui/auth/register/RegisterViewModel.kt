package com.mrh.storyapp.ui.auth.register

import androidx.lifecycle.ViewModel
import com.mrh.storyapp.data.stories.StoryRepository

class RegisterViewModel(private val storyRepository: StoryRepository) : ViewModel() {
    fun register(name: String, email: String, password: String) = storyRepository.register(name, email, password)
}