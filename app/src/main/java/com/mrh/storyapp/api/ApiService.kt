package com.mrh.storyapp.api

import com.mrh.storyapp.data.stories.FileUploadResponse
import com.mrh.storyapp.data.stories.ResponseDetailStory
import com.mrh.storyapp.data.stories.ResponseStories
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import  retrofit2.http.*

interface ApiService {
    @FormUrlEncoded
    @POST("login")
    fun login(
        @Field("email") email: String,
        @Field("password") password: String
    ): Call<ResponseLogin>

    @FormUrlEncoded
    @POST("register")
    fun register(
        @Field("name") name: String,
        @Field("email") email: String,
        @Field("password") password: String
    ): Call<ResponseRegister>


    @GET("stories")
    fun getAllStories() : Call<ResponseStories>

    @GET("stories/{id}")
    fun getDetailStory(
        @Path("id") id :String
    ): Call<ResponseDetailStory>

    @Multipart
    @POST("stories")
    fun addStory(
        @Part file: MultipartBody.Part,
        @Part("description") description: RequestBody
    ): Call<FileUploadResponse>
}