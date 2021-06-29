package com.ivan.animals

import android.app.Application
import com.ivan.animals.di.PrefsModule
import com.ivan.animals.utils.SharedPreferencesHelper

class PrefsModuleTest(val mockPrefs: SharedPreferencesHelper): PrefsModule() {

    override fun provideSharedPreferences(app: Application): SharedPreferencesHelper {
        return mockPrefs
    }
}