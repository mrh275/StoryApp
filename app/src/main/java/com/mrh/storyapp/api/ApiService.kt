package com.mrh.storyapp.api

import com.mrh.storyapp.data.stories.ResponseStories
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

    @Headers("Authorization: token 12345")
    @GET("stories")
    fun getAllStories() : Call<ResponseStories>
}