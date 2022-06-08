package com.capstone.pet2home.ui.settings

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import com.capstone.pet2home.R
import com.capstone.pet2home.databinding.ActivitySettingsBinding
import com.capstone.pet2home.helper.SettingsHelper
import com.capstone.pet2home.preference.UserPreference
import com.capstone.pet2home.ui.ViewModelFactory
import com.capstone.pet2home.ui.login.LoginActivity
import com.capstone.pet2home.ui.settings.changelanguage.ChangeLanguageActivity
import com.capstone.pet2home.ui.settings.changepassword.ChangePasswordActivity
import com.capstone.pet2home.ui.settings.editprofile.EditProfileActivity

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class SettingsActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySettingsBinding
    private lateinit var settingsViewModel: SettingsViewModel
    private val settingsHelper = SettingsHelper(this)
    private lateinit var languageNow: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        languageNow = settingsHelper.getLanguageActive()
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val actionBar: ActionBar? = supportActionBar
        actionBar?.apply {
            setTitle(R.string.title_settings)
            setDisplayHomeAsUpEnabled(true)
            setHomeAsUpIndicator(R.drawable.ic_arrow_back_24)
        }

        setupViewModel()
        buttonListener()
    }

    override fun onResume() {
        super.onResume()
        if(languageNow != settingsHelper.getLanguageActive()){
            recreate()
        }
    }

    private fun setupViewModel() {
        settingsViewModel = ViewModelProvider(this,
            ViewModelFactory(UserPreference.getInstance(dataStore), this)
        )[SettingsViewModel::class.java]
    }

    private fun buttonListener(){
        binding.cvLanguage.setOnClickListener{
            val intent = Intent(this, ChangeLanguageActivity::class.java)
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

        binding.btnLogout.setOnClickListener {
            settingsViewModel.logout()
            val intent = Intent(this, LoginActivity::class.java)
            intent.putExtra(LoginActivity.EXTRA_MESSAGE_ALERT,getString(R.string.success_logout))
            startActivity(intent)
            finish()
        }
    }



    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
            return true
        }
        return super.onContextItemSelected(item)
    }
}