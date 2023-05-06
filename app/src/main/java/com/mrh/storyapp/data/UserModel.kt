package com.mrh.storyapp.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class UserModel(
    var token: String? = null,
    var userId: String? = null,
    var name: String? = null
) : Parcelable
