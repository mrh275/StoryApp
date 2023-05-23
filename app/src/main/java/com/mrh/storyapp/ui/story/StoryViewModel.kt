package com.mrh.storyapp.ui.story

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.mrh.storyapp.api.ApiConfig
import com.mrh.storyapp.data.stories.ListStoryItem
import com.mrh.storyapp.data.stories.ResponseDetailStory
import com.mrh.storyapp.data.stories.StoryRepository
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class StoryViewModel(storyRepository: StoryRepository) : ViewModel() {

    val story: LiveData<PagingData<ListStoryItem>> =
        storyRepository.getStory().cachedIn(viewModelScope)

    private var detailStory: MutableLiveData<ResponseDetailStory> = MutableLiveData()
    fun detailStory(id: String, context: Context) {
        val client =
            ApiConfig.getApiWithToken(context)
                .getDetailStory(id)
        client.enqueue(object : Callback<ResponseDetailStory> {
            override fun onResponse(
                call: Call<ResponseDetailStory>,
                response: Response<ResponseDetailStory>
            ) {
                if (response.isSuccessful) {
                    detailStory.postValue(response.body())
                }
            }

            override fun onFailure(call: Call<ResponseDetailStory>, t: Throwable) {
                Log.e("StoryViewModel", "onFailure : ${t.message}")
            }
        })
    }

    fun getDetailStoryObserve(): LiveData<ResponseDetailStory> {
        return detailStory
    }
}
