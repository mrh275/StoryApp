package com.mrh.storyapp.data.stories

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import androidx.paging.*
import com.mrh.storyapp.api.ApiService
import com.mrh.storyapp.data.StoryRemoteMediator
import com.mrh.storyapp.data.login.ResponseLogin
import com.mrh.storyapp.data.register.ResponseRegister
import com.mrh.storyapp.database.StoryDatabase
import okhttp3.MultipartBody
import okhttp3.RequestBody

class StoryRepository(private val storyDatabase: StoryDatabase, private val apiService: ApiService) {
    fun getStory(): LiveData<PagingData<ListStoryItem>> {
        @OptIn(ExperimentalPagingApi::class)
        return Pager(
            config = PagingConfig(
                pageSize = 5
            ),
            remoteMediator = StoryRemoteMediator(storyDatabase, apiService),
            pagingSourceFactory = {
                storyDatabase.storyDao().getAllStory()
            }
        ).liveData
    }

    fun getStoryWithLocation(): LiveData<com.mrh.storyapp.data.Result<ResponseStories>> = liveData {
        emit(com.mrh.storyapp.data.Result.Loading)
        try {
            val response = apiService.getStoriesWithLocation(1)
            emit(com.mrh.storyapp.data.Result.Success(response))
        } catch (e: Exception) {
            Log.e("StoryViewModel", "GetStoryWithLocation : ${e.message.toString()}")
            emit(com.mrh.storyapp.data.Result.Error(e.message.toString()))
        }
    }

    fun addStory(imageMultiPart: MultipartBody.Part, description: RequestBody, lat: Double, lon: Double): LiveData<com.mrh.storyapp.data.Result<ResponseAddStory>> = liveData {
        emit(com.mrh.storyapp.data.Result.Loading)
        try {
            val response = apiService.addStory(imageMultiPart, description, lat, lon)
            emit(com.mrh.storyapp.data.Result.Success(response))
        } catch (e: Exception) {
            Log.e("AddStoryViewModel", "addStory : ${e.message.toString()}")
            emit(com.mrh.storyapp.data.Result.Error(e.message.toString()))
        }
    }

    fun login(email: String, password: String): LiveData<com.mrh.storyapp.data.Result<ResponseLogin>> = liveData {
        emit(com.mrh.storyapp.data.Result.Loading)
        try {
            val response = apiService.login(email, password)
            emit(com.mrh.storyapp.data.Result.Success(response))
        } catch (e: Exception) {
            Log.e("LoginViewModel", "Login : ${e.message.toString()}")
            emit(com.mrh.storyapp.data.Result.Error(e.message.toString()))
        }
    }

    fun register(name: String, email: String, password: String): LiveData<com.mrh.storyapp.data.Result<ResponseRegister>> = liveData {
        emit(com.mrh.storyapp.data.Result.Loading)
        try {
            val response = apiService.register(name, email, password)
            emit(com.mrh.storyapp.data.Result.Success(response))
        } catch (e: Exception) {
            Log.e("RegisterViewModel", "Register : ${e.message.toString()}")
            emit(com.mrh.storyapp.data.Result.Error(e.message.toString()))
        }
    }
}