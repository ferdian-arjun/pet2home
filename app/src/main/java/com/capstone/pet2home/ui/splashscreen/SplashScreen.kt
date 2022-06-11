package com.capstone.pet2home.ui.splashscreen

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import com.capstone.pet2home.MainActivity
import com.capstone.pet2home.databinding.ActivitySplashScreenBinding
import com.capstone.pet2home.helper.SettingsHelper
import com.capstone.pet2home.preference.SettingsPreference
import com.capstone.pet2home.preference.UserPreference
import com.capstone.pet2home.ui.ViewModelFactory
import com.capstone.pet2home.ui.login.LoginActivity
import com.capstone.pet2home.ui.onboardingpage.OnBoardingPage

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

@SuppressLint("CustomSplashScreen")
class SplashScreen : AppCompatActivity() {
    private lateinit var binding: ActivitySplashScreenBinding
    private lateinit var settingsPreference: SettingsPreference
    private lateinit var splashScreenViewModel: SplashScreenViewModel

    private val settingHelper = SettingsHelper(this)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        Handler(mainLooper).postDelayed({
            if(settingHelper.getOnBoardingPage() == true){
                val intent = Intent(this, OnBoardingPage::class.java)
                startActivity(intent)
                finish()
            }else{
                setupViewModel()
            }
        }, 3000)
    }

    private fun setupViewModel() {
        splashScreenViewModel = ViewModelProvider(this,
            ViewModelFactory(UserPreference.getInstance(dataStore), this)
        )[SplashScreenViewModel::class.java]

        splashScreenViewModel.getUser().observe(this){
            if (it.token.isEmpty()) {
                startActivity(Intent(this, LoginActivity::class.java))
                finish()
            }else{
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()
            }
        }
    }
}