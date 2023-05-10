package com.mrh.storyapp.data.stories

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.mrh.storyapp.api.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class StoryViewModel : ViewModel() {

    private var listStory: MutableLiveData<List<ListStoryItem>> = MutableLiveData()
    private var detailStory: MutableLiveData<ResponseDetailStory> = MutableLiveData()

    companion object {
        private const val TAG = "StoryViewModel"
    }

    fun getListStoryObserve(): MutableLiveData<List<ListStoryItem>> {
        return listStory
    }

    fun getDetailStoryObserve(): MutableLiveData<ResponseDetailStory> {
        return detailStory
    }
    fun findStories() {
        val client =
            ApiConfig.getApiWithToken("eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VySWQiOiJ1c2VyLUloTTdWM0tXU0VWc29mNW0iLCJpYXQiOjE2ODM0NDQ4MTl9.-iOOHW5gpujl7p-O5_gFSc30-EurMzcDTk9MwDQCmxs")
                .getAllStories()
        client.enqueue(object : Callback<ResponseStories> {
            override fun onResponse(
                call: Call<ResponseStories>,
                response: Response<ResponseStories>
            ) {
                if(response.isSuccessful) {
                    listStory.postValue(response.body()?.listStory)
                } else {
                    Log.e(TAG, "onFailure : ${response.message()}")
                }
            }

            override fun onFailure(call: Call<ResponseStories>, t: Throwable) {
                Log.e(TAG, "onFailure : ${t.message}")
            }

        })
    }

    fun setDetailStory(id: String) {
        val client =
            ApiConfig.getApiWithToken("eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VySWQiOiJ1c2VyLUloTTdWM0tXU0VWc29mNW0iLCJpYXQiOjE2ODM0NDQ4MTl9.-iOOHW5gpujl7p-O5_gFSc30-EurMzcDTk9MwDQCmxs")
                .getDetailStory(id)
        client.enqueue(object : Callback<ResponseDetailStory> {
            override fun onResponse(
                call: Call<ResponseDetailStory>,
                response: Response<ResponseDetailStory>
            ) {
                if(response.isSuccessful) {
                    detailStory.postValue(response.body())
                }
            }

            override fun onFailure(call: Call<ResponseDetailStory>, t: Throwable) {
                Log.e(TAG, "onFailure : ${t.message}")
            }

        })
    }
}
