package com.mrh.storyapp.api

import com.mrh.storyapp.data.login.ResponseLogin
import com.mrh.storyapp.data.register.ResponseRegister
import com.mrh.storyapp.data.stories.ResponseAddStory
import com.mrh.storyapp.data.stories.ResponseDetailStory
import com.mrh.storyapp.data.stories.ResponseStories
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*

interface ApiService {
    @FormUrlEncoded
    @POST("login")
    suspend fun login(
        @Field("email") email: String,
        @Field("password") password: String
    ): ResponseLogin

    @FormUrlEncoded
    @POST("register")
    suspend fun register(
        @Field("name") name: String,
        @Field("email") email: String,
        @Field("password") password: String
    ): ResponseRegister


    @GET("stories")
    suspend fun getAllStories(
        @Query("page") page: Int,
        @Query("size") size: Int
    ) : ResponseStories

    @GET("stories?location=1")
    suspend fun getStoriesWithLocation(
        @Query("location") location: Int
    ) : ResponseStories

    @GET("stories/{id}")
    fun getDetailStory(
        @Path("id") id :String
    ): Call<ResponseDetailStory>

    @Multipart
    @POST("stories")
    suspend fun addStory(
        @Part file: MultipartBody.Part,
        @Part("description") description: RequestBody,
        @Part("lat") lat: Double,
        @Part("lon") lon: Double
    ): ResponseAddStory
}