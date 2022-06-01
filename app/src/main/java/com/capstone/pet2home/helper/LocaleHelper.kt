package com.capstone.pet2home.helper

import android.app.Activity
import android.content.res.Configuration
import android.content.res.Resources
import com.capstone.pet2home.model.SettingsModel
import com.capstone.pet2home.ui.settings.SettingsPreference
import java.util.*

class LocaleHelper(private val activity: Activity) {
     fun setLanguage(lang: String) {
        val locale = Locale(lang)
        val resources: Resources = activity.resources
        val config: Configuration = resources.configuration
        config.setLocale(locale)
        resources.updateConfiguration(config,resources.displayMetrics)

        saveLanguageSettings(lang)
    }

    private fun saveLanguageSettings(lang: String) {
        val settingsPreference = SettingsPreference(activity)
        val settingsModel = SettingsModel()

        settingsModel.language = lang

        settingsPreference.setSettings(settingsModel)
    }

    fun loadLocal() {
        val settingsPreference = SettingsPreference(activity)
        val settingsModel = settingsPreference.getSettings()
        val language = settingsModel.language.toString()

        setLanguage(language)
    }

    fun getLanguageActive(): String{
        val settingsPreference = SettingsPreference(activity)
        val settingsModel = settingsPreference.getSettings()
        return settingsModel.language.toString()
    }
}