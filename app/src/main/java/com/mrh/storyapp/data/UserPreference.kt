package com.mrh.storyapp.data

import android.content.Context

class UserPreference(context: Context) {

    companion object {
        const val PREFS_NAME = "prefs_name"
        const val TOKEN = "token"
        const val USER_ID = "user_id"
        const val NAME = "name"
    }

    private val preferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    fun setAuthSession(value: UserModel) {
        val editor = preferences.edit()
        editor.putString(TOKEN, value.token)
        editor.putString(USER_ID, value.userId)
        editor.putString(NAME, value.name)
        editor.apply()
    }

    fun getAuthSession(): UserModel {
        val model = UserModel()
        model.token = preferences.getString(TOKEN, "")
        model.userId = preferences.getString(USER_ID, "")
        model.name = preferences.getString(NAME, "")

        return model
    }

    fun clearAuthSession(){
        val editor = preferences.edit()
        editor.clear()
        editor.apply()
    }
}