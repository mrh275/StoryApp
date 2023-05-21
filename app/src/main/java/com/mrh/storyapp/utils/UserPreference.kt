package com.mrh.storyapp.utils

import android.content.Context
import android.content.SharedPreferences

object UserPreference {

        private const val PREFS_NAME = "prefs_name"
        private const val TOKEN = "token"

    private fun initPreference(context: Context) : SharedPreferences {
        return context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    }


    fun setAuthSession(token: String, context: Context) {
        val editor = initPreference(context).edit()
        editor.putString(TOKEN, token)
        editor.apply()
    }

    fun getAuthSession(context: Context): String {
        return initPreference(context).getString("token", "").toString()
    }

    fun clearAuthSession(context: Context){
        val editor = initPreference(context).edit()
        editor.clear()
        editor.apply()
    }
}