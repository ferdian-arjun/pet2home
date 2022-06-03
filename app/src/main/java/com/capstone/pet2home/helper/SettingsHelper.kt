package com.capstone.pet2home.helper

import android.app.Activity
import android.content.res.Configuration
import android.content.res.Resources
import com.capstone.pet2home.model.SettingsModel
import com.capstone.pet2home.preference.SettingsPreference
import java.util.*

class SettingsHelper(private val activity: Activity) {

    private fun getSettingModelPreference(): SettingsModel {
        val settingsPreference = SettingsPreference(activity)
        return  settingsPreference.getSettings()
    }

    private fun saveSettingsPreference(language: String? = null, isOnboardPage: Boolean? = null){
        val settingsPreference = SettingsPreference(activity)
        val settingsModel = settingsPreference.getSettings()

        settingsModel.language = language ?: settingsModel.language
        settingsModel.isOnboardPage = isOnboardPage ?: settingsModel.isOnboardPage


        settingsPreference.setSettings(settingsModel)
    }

    fun setLanguage(lang: String) {
        val locale = Locale(lang)
        val resources: Resources = activity.resources
        val config: Configuration = resources.configuration
        config.setLocale(locale)
        resources.updateConfiguration(config,resources.displayMetrics)

        saveSettingsPreference(language = lang)
    }

    fun loadLocal() {
        val language = getSettingModelPreference().language.toString()
        setLanguage(language)
    }

    fun getLanguageActive(): String{
        return getSettingModelPreference().language.toString()
    }


    fun saveOnBoardPage(value: Boolean){
        saveSettingsPreference(isOnboardPage = value)
    }

    fun getOnBoardingPage(): Boolean? {
        return  getSettingModelPreference().isOnboardPage
    }
}