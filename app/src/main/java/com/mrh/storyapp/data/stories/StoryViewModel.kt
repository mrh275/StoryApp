package com.mrh.storyapp.data.stories

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.mrh.storyapp.api.ApiConfig
import com.mrh.storyapp.data.UserPreference
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class StoryViewModel : ViewModel() {

    private val _listStory = MutableLiveData<List<ListStoryItem>>()
    val listStory: LiveData<List<ListStoryItem>> = _listStory

    companion object {
        private const val TAG = "StoryViewModel"
    }

    init {
        findStories()
    }

    private fun findStories() {
        val client = ApiConfig.getApiWithToken("eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VySWQiOiJ1c2VyLUloTTdWM0tXU0VWc29mNW0iLCJpYXQiOjE2ODM0NDQ4MTl9.-iOOHW5gpujl7p-O5_gFSc30-EurMzcDTk9MwDQCmxs").getAllStories()
        client.enqueue(object : Callback<ResponseStories> {
            override fun onResponse(
                call: Call<ResponseStories>,
                response: Response<ResponseStories>
            ) {
                if(response.isSuccessful) {
                    _listStory.value = response.body()?.listStory
                } else {
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<ResponseStories>, t: Throwable) {
                Log.e(TAG, "onFailure: ${t.message}")
            }

        })
    }
}