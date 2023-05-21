package com.mrh.storyapp.data.stories

import com.google.gson.annotations.SerializedName

data class ResponseAddStory(
    @field:SerializedName("error")
    val error: Boolean,

    @field:SerializedName("message")
    val message: String
)
