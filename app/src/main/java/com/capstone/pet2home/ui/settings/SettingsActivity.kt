package com.capstone.pet2home.ui.settings

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.core.graphics.translationMatrix
import com.capstone.pet2home.R
import com.capstone.pet2home.databinding.ActivitySettingsBinding
import com.capstone.pet2home.ui.settings.changepassword.ChangePasswordActivity
import com.capstone.pet2home.ui.settings.editprofile.EditProfileActivity


class SettingsActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySettingsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val actionBar: ActionBar? = supportActionBar
        actionBar?.apply {
            setTitle(R.string.title_settings)
            setDisplayHomeAsUpEnabled(true)
            setHomeAsUpIndicator(R.drawable.ic_arrow_back_24)
        }

        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.cvEditProfile.setOnClickListener{
            val intent = Intent(this, EditProfileActivity::class.java,)
            startActivity(intent)
        }

        binding.cvChangePassword.setOnClickListener{
            val intent = Intent(this, ChangePasswordActivity::class.java)
            startActivity(intent)
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