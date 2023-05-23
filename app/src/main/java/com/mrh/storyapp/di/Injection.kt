package com.mrh.storyapp.di

import android.content.Context
import com.mrh.storyapp.api.ApiConfig
import com.mrh.storyapp.data.stories.StoryRepository
import com.mrh.storyapp.database.StoryDatabase

object Injection {
    fun provideRepository(context: Context): StoryRepository {
        val database = StoryDatabase.getDatabase(context)
        val apiService = ApiConfig.getApiWithToken(context)
        return StoryRepository(database, apiService)
    }
}