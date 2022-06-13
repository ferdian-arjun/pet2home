package com.capstone.pet2home.ui.onboardingpage

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.capstone.pet2home.databinding.ActivityOnBoardingPageBinding
import com.capstone.pet2home.ui.login.LoginActivity
import com.capstone.pet2home.ui.register.RegisterActivity

class OnBoardingPage : AppCompatActivity() {
    private lateinit var binding: ActivityOnBoardingPageBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOnBoardingPageBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        buttonListener()
    }

    private fun buttonListener(){
        binding.buttonLogin.setOnClickListener{
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }

        binding.buttonRegister.setOnClickListener{
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}