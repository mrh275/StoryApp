package com.mrh.storyapp.data.stories

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.mrh.storyapp.api.ApiConfig
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class StoryViewModel() : ViewModel() {

    private var listStory: MutableLiveData<List<ListStoryItem>> = MutableLiveData()
    private var detailStory: MutableLiveData<ResponseDetailStory> = MutableLiveData()
    private var uploadResponse: MutableLiveData<FileUploadResponse> = MutableLiveData()

    companion object {
        private const val TAG = "StoryViewModel"
    }

    fun getListStoryObserve(): MutableLiveData<List<ListStoryItem>> {
        return listStory
    }

    fun getDetailStoryObserve(): MutableLiveData<ResponseDetailStory> {
        return detailStory
    }

    fun getUploadResult(): MutableLiveData<FileUploadResponse> {
        return uploadResponse
    }
    fun findStories(token: String) {
        val client =
            ApiConfig.getApiWithToken(token)
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

    fun setDetailStory(id: String, token: String) {
        val client =
            ApiConfig.getApiWithToken(token)
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

    fun addNewStory(imageMultiPart: MultipartBody.Part, description: RequestBody, token: String) {
        val client =
            ApiConfig.getApiWithToken(token)
                .addStory(imageMultiPart, description)
        client.enqueue(object : Callback<FileUploadResponse> {
            override fun onResponse(
                call: Call<FileUploadResponse>,
                response: Response<FileUploadResponse>
            ) {
                if(response.isSuccessful) {
                    val responseBody = response.body()
                    if(responseBody != null && !responseBody.error) {
                        uploadResponse.postValue(responseBody)
                    } else {
                        Log.e("onFailure", responseBody!!.message)
                    }
                }
            }

            override fun onFailure(call: Call<FileUploadResponse>, t: Throwable) {
                Log.e("onFailure", t.message.toString())
            }

        })
    }
}
