package com.mrh.storyapp.ui.auth.login

import androidx.lifecycle.ViewModel
import com.mrh.storyapp.data.stories.StoryRepository

class LoginViewModel(private val storyRepository: StoryRepository) : ViewModel() {

    fun login(email: String, password: String) = storyRepository.login(email, password)
}