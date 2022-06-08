package com.capstone.pet2home.preference

import android.content.Context
import com.capstone.pet2home.model.SettingsModel

class SettingsPreference(context: Context) {
    companion object{
        private const val PREFS_NAME = "settings_pref"
        private const val LANGUAGE = "language"
        private const val IS_ONBOARD_PAGE = "isOnboardPage"
    }

    private val preference = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    fun setSettings(value: SettingsModel){
        val editor = preference.edit()
        editor.putString(LANGUAGE, value.language)
        value.isOnboardPage?.let { editor.putBoolean(IS_ONBOARD_PAGE, it) }

        editor.apply()
    }

    fun getSettings(): SettingsModel{
        val model = SettingsModel()
        model.language = preference.getString(LANGUAGE, "en")
        model.isOnboardPage = preference.getBoolean(IS_ONBOARD_PAGE, true)
        return model
    }
}