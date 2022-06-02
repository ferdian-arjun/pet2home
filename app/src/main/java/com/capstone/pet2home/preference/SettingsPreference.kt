package com.capstone.pet2home.preference

import android.content.Context
import com.capstone.pet2home.model.SettingsModel

class SettingsPreference(context: Context) {
    companion object{
        private const val PREFS_NAME = "settings_pref"
        private const val LANGUAGE = "language"
    }

    private val preference = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    fun setSettings(value: SettingsModel){
        val editor = preference.edit()
        editor.putString(LANGUAGE, value.language)

        editor.apply()
    }

    fun getSettings(): SettingsModel{
        val model = SettingsModel()
        model.language = preference.getString(LANGUAGE, "en")
        return model
    }
}