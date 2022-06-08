package com.capstone.pet2home.ui.register

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import com.capstone.pet2home.R
import com.capstone.pet2home.databinding.ActivityRegisterBinding
import com.capstone.pet2home.helper.ValidationHelper
import com.capstone.pet2home.preference.UserPreference
import com.capstone.pet2home.ui.ViewModelFactory
import com.capstone.pet2home.ui.login.LoginActivity
import com.capstone.pet2home.ui.login.LoginActivity.Companion.EXTRA_MESSAGE_ALERT
import com.tapadoo.alerter.Alerter
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding
    private lateinit var registerViewModel: RegisterViewModel
    private val validationHelper: ValidationHelper = ValidationHelper()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()
        binding.btnRegister.isEnabled = false

        focusListener()
        buttonListener()
        setupViewModel()
        setupAction()
    }

    private fun focusListener() {
        binding.edtName.addTextChangedListener {
            binding.layoutName.helperText = validationHelper.validFullName(binding.edtName.text.toString())
            enableButton()
        }

        binding.edtEmail.addTextChangedListener {
            binding.layoutEmail.helperText = validationHelper.validEmail(binding.edtEmail.text.toString())
            enableButton()
        }

        binding.edtPassword.addTextChangedListener {
            binding.layoutPassword.helperText = validationHelper.validPassword(binding.edtPassword.text.toString())
            enableButton()
        }
    }

    private fun buttonListener(){
        binding.tvLogin.setOnClickListener{
            finish()
        }
    }

    private fun enableButton() {
        val validFullName = binding.layoutName.helperText == null
        val validEmail = binding.layoutEmail.helperText == null
        val validPassword = binding.layoutPassword.helperText == null
        val textFullName = !binding.edtName.text.isNullOrBlank()
        val textEmail = !binding.edtEmail.text.isNullOrBlank()
        val textPassword = !binding.edtPassword.text.isNullOrBlank()

        binding.btnRegister.isEnabled = (validFullName && validEmail && validPassword && textFullName && textEmail && textPassword)
    }

    private fun setupViewModel() {
        registerViewModel = ViewModelProvider(
            this, ViewModelFactory(UserPreference.getInstance(dataStore),this)
        )[RegisterViewModel::class.java]

        registerViewModel.showLoading.observe(this) {showLoading(it)}
    }

    private fun setupAction() {
        binding.apply {
            btnRegister.setOnClickListener {
                val fullName = edtName.text.toString()
                val email = edtEmail.text.toString()
                val password = edtPassword.text.toString()

                val jsonObject = JSONObject()
                jsonObject.put(FULL_NAME, fullName)
                jsonObject.put(EMAIL, email)
                jsonObject.put(PASSWORD, password)
                val jsonObjectString = jsonObject.toString()
                val requestBody = jsonObjectString.toRequestBody("application/json".toMediaTypeOrNull())

                registerViewModel.registerUser(requestBody)

                registerViewModel.returnResponse.observe(this@RegisterActivity){
                    if(it.status == 201){
                        val intent = Intent(this@RegisterActivity, LoginActivity::class.java)
                        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                        intent.putExtra(EXTRA_MESSAGE_ALERT,it.message)
                        startActivity(intent)
                        finish()
                    }else{
                        if(registerViewModel.showLoading.value == false){
                            Alerter.create(this@RegisterActivity)
                                .setTitle(getString(R.string.failed_register))
                                .setText(it.message)
                                .setBackgroundColorRes(R.color.red)
                                .setDuration(1500)
                                .setIcon(R.drawable.ic_error)
                                .show()
                        }
                    }
                }
            }
        }
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBar.visibility = View.VISIBLE
            window.setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
        } else {
            binding.progressBar.visibility = View.GONE
            window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }


    companion object {
        private const val FULL_NAME = "fullname"
        private const val EMAIL = "email"
        private const val PASSWORD = "password"

    }
}