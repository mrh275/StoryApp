package com.mrh.storyapp.utils

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.mrh.storyapp.di.Injection
import com.mrh.storyapp.ui.auth.login.LoginViewModel
import com.mrh.storyapp.ui.auth.register.RegisterViewModel
import com.mrh.storyapp.ui.story.StoryViewModel
import com.mrh.storyapp.ui.story.addstory.AddStoryViewModel
import com.mrh.storyapp.ui.story.maps.StoryMapsViewModel

class ViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(StoryViewModel::class.java) -> {
                StoryViewModel(Injection.provideRepository(context)) as T
            }
            modelClass.isAssignableFrom(AddStoryViewModel::class.java) -> {
                AddStoryViewModel(Injection.provideRepository(context)) as T
            }
            modelClass.isAssignableFrom(StoryMapsViewModel::class.java) -> {
                StoryMapsViewModel(Injection.provideRepository(context)) as T
            }
            modelClass.isAssignableFrom(LoginViewModel::class.java) -> {
                LoginViewModel(Injection.provideRepository(context)) as T
            }
            modelClass.isAssignableFrom(RegisterViewModel::class.java) -> {
                RegisterViewModel(Injection.provideRepository(context)) as T
            }
            else -> throw java.lang.IllegalArgumentException("Unknown ViewModel class")
        }

    }
}