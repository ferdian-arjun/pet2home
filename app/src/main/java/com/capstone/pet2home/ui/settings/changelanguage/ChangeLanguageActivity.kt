package com.capstone.pet2home.ui.settings.changelanguage

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import com.capstone.pet2home.R
import com.capstone.pet2home.databinding.ActivityChangeLanguageBinding
import com.capstone.pet2home.helper.LocaleHelper

class ChangeLanguageActivity : AppCompatActivity() {
    private lateinit var binding: ActivityChangeLanguageBinding
    private val localeHelper = LocaleHelper(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChangeLanguageBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val actionBar: ActionBar? = supportActionBar
        actionBar?.apply {
            setTitle(R.string.title_change_language)
            setDisplayHomeAsUpEnabled(true)
            setHomeAsUpIndicator(R.drawable.ic_arrow_back_24)
        }

        visibilityLanguage()
        selectLanguage()

    }

    private fun visibilityLanguage() {
        val lang =  localeHelper.getLanguageActive()
        if(lang == "in"){
            binding.viIndonesian.visibility = View.VISIBLE
        }
        if(lang == "en"){
            binding.viEnglish.visibility = View.VISIBLE
        }
    }



    private fun selectLanguage() {
        binding.apply {
            cvIndonesian.setOnClickListener {
                localeHelper.setLanguage("in")
                recreate()
            }

            cvEnglish.setOnClickListener {
                localeHelper.setLanguage("en")
                recreate()
            }
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