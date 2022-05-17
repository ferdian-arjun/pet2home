package com.capstone.pet2home.ui.splashscreen

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.capstone.pet2home.databinding.ActivitySplashScreenBinding
import com.capstone.pet2home.ui.onboardingpage.OnBoardingPage

@SuppressLint("CustomSplashScreen")
class SplashScreen : AppCompatActivity() {
    private lateinit var binding: ActivitySplashScreenBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        Handler(mainLooper).postDelayed({
            val intent = Intent(this, OnBoardingPage::class.java)
            startActivity(intent)
            finish()
        }, 3000)
    }
}