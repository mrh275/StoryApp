package com.mrh.storyapp.data

import android.content.Context

internal class UserPreference(context: Context) {
    companion object {
        private const val PREFS_NAME = "user_prefs"
        private const val TOKEN = "token"
        private const val USER_ID = "user_id"
        private const val NAME = "name"
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