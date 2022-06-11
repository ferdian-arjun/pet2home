package com.capstone.pet2home.ui.splashscreen

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.capstone.pet2home.model.UserModel
import com.capstone.pet2home.preference.UserPreference

class SplashScreenViewModel(private val pref: UserPreference): ViewModel() {
    fun getUser(): LiveData<UserModel> {
        return pref.getUser().asLiveData()
    }
}