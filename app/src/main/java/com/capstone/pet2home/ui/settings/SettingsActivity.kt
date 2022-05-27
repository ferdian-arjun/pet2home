package com.capstone.pet2home.ui.settings

import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.view.MenuItem
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import com.capstone.pet2home.R
import com.capstone.pet2home.databinding.ActivitySettingsBinding
import com.capstone.pet2home.ui.settings.changepassword.ChangePasswordActivity
import com.capstone.pet2home.ui.settings.editprofile.EditProfileActivity


class SettingsActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySettingsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val actionBar: ActionBar? = supportActionBar
        actionBar?.apply {
            setTitle(R.string.title_settings)
            setDisplayHomeAsUpEnabled(true)
            setHomeAsUpIndicator(R.drawable.ic_arrow_back_24)
        }

        buttonListener()
    }

    private fun buttonListener(){
        binding.cvLanguage.setOnClickListener{
            val intent = Intent(Settings.ACTION_LOCALE_SETTINGS)
            startActivity(intent)
        }

        binding.cvEditProfile.setOnClickListener{
            val intent = Intent(this, EditProfileActivity::class.java)
            startActivity(intent)
        }

        binding.cvChangePassword.setOnClickListener{
            val intent = Intent(this, ChangePasswordActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            return true
        }
        return super.onContextItemSelected(item)
    }
}