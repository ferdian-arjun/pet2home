package com.capstone.pet2home.ui.settings.changepassword

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import com.capstone.pet2home.R
import com.capstone.pet2home.databinding.ActivityChangePasswordBinding
import com.capstone.pet2home.helper.ValidationHelper
import com.capstone.pet2home.preference.UserPreference
import com.capstone.pet2home.ui.ViewModelFactory
import com.tapadoo.alerter.Alerter
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class ChangePasswordActivity : AppCompatActivity() {
    private lateinit var binding: ActivityChangePasswordBinding
    private lateinit var changePasswordViewModel: ChangePasswordViewModel
    private val validationHelper: ValidationHelper = ValidationHelper()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val actionBar: ActionBar? = supportActionBar
        actionBar?.apply {
            setTitle(R.string.title_change_password)
            setDisplayHomeAsUpEnabled(true)
            setHomeAsUpIndicator(R.drawable.ic_arrow_back_24)
        }
        setContentView(R.layout.activity_change_password)

        binding = ActivityChangePasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.edtOldPassword.requestFocus()

        focusListener()
        setupViewModel()
        setupAction()

    }

    private fun focusListener() {
        binding.edtOldPassword.addTextChangedListener {
            binding.layoutOldPassword.helperText = validationHelper.validPassword(binding.edtOldPassword.text.toString())
            enableButton()
        }

        binding.edtNewPassword.addTextChangedListener {
            binding.layoutNewPassword.helperText = validationHelper.validPassword(binding.edtNewPassword.text.toString())
            enableButton()
        }
    }

    private fun enableButton() {
        val validOldPassword = binding.layoutOldPassword.helperText == null
        val validNewPassword = binding.layoutNewPassword.helperText == null
        val textOldPassword = !binding.edtOldPassword.text.isNullOrBlank()
        val textNewPassword = !binding.edtNewPassword.text.isNullOrBlank()

        binding.btnUpdatePassword.isEnabled = (validOldPassword && validNewPassword && textOldPassword && textNewPassword)
    }

    private fun setupViewModel() {
        changePasswordViewModel = ViewModelProvider(
            this, ViewModelFactory(UserPreference.getInstance(dataStore),this)
        )[ChangePasswordViewModel::class.java]


        changePasswordViewModel.showLoading.observe(this) {showLoading(it)}
    }


    private fun setupAction() {
        binding.apply {
            btnUpdatePassword.setOnClickListener {
                val newPassword = edtNewPassword.text.toString()
                val oldPassword = edtOldPassword.text.toString()

                val jsonObject = JSONObject()
                jsonObject.put(NEW_PASSWORD, newPassword)
                jsonObject.put(OLD_PASSWORD, oldPassword)
                val jsonObjectString = jsonObject.toString()
                val requestBody = jsonObjectString.toRequestBody("application/json".toMediaTypeOrNull())


                changePasswordViewModel.getUser().observe(this@ChangePasswordActivity) {
                    changePasswordViewModel.sendData(it.userId, it.token, requestBody)
                }

                changePasswordViewModel.returnResponse.observe(this@ChangePasswordActivity){
                    if(it.status == 200){
                        alert(true, it.message)
                    }else{
                        alert(false, it.message)
                    }

                    //reset Form
                    edtNewPassword.text = null
                    edtOldPassword.text = null
                    layoutOldPassword.isHelperTextEnabled = false
                    layoutNewPassword.isHelperTextEnabled = false
                    edtOldPassword.requestFocus()
                }
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

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBar.visibility = View.VISIBLE
            binding.viewLoading.visibility = View.VISIBLE
            window.setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
        } else {
            binding.progressBar.visibility = View.GONE
            binding.viewLoading.visibility = View.GONE
            window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
        }
    }

    private fun alert(success: Boolean, message: String,) {
        if(success){
            Alerter.create(this)
                .setTitle(getString(R.string.success))
                .setText(message)
                .setBackgroundColorRes(R.color.teal_200)
                .setDuration(1500)
                .setIcon(R.drawable.ic_info_24)
                .show()
        }else{
            Alerter.create(this)
                .setTitle(getString(R.string.failed))
                .setText(message)
                .setBackgroundColorRes(R.color.red)
                .setDuration(1500)
                .setIcon(R.drawable.ic_error)
                .show()
        }
    }

    companion object {
        const val NEW_PASSWORD = "newPassword"
        const val OLD_PASSWORD = "oldPassword"
    }
}