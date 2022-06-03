package com.capstone.pet2home.ui.splashscreen

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import com.capstone.pet2home.MainActivity
import com.capstone.pet2home.databinding.ActivitySplashScreenBinding
import com.capstone.pet2home.helper.SettingsHelper
import com.capstone.pet2home.preference.SettingsPreference
import com.capstone.pet2home.ui.onboardingpage.OnBoardingPage

@SuppressLint("CustomSplashScreen")
class SplashScreen : AppCompatActivity() {
    private lateinit var binding: ActivitySplashScreenBinding
    private lateinit var settingsPreference: SettingsPreference
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
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()
            }
        }, 3000)
    }
}