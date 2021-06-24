package com.ivan.animals.utils

import android.content.Context
import android.preference.PreferenceManager
import androidx.annotation.NonNull

class SharedPreferencesHelper(val context: Context) {

    private val API_KEY = "Api key"

    private val prefs = PreferenceManager.getDefaultSharedPreferences(context.applicationContext)

    fun saveApiKey(key: String) {
        prefs.edit().putString(API_KEY, key).apply()
    }

    @NonNull
    fun getApiKey() = prefs.getString(API_KEY, "")
}