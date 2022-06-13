package com.capstone.pet2home.ui.login

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
import com.capstone.pet2home.MainActivity
import com.capstone.pet2home.R
import com.capstone.pet2home.databinding.ActivityLoginBinding
import com.capstone.pet2home.helper.ValidationHelper
import com.capstone.pet2home.model.UserModel
import com.capstone.pet2home.preference.UserPreference
import com.capstone.pet2home.ui.ViewModelFactory
import com.capstone.pet2home.ui.register.RegisterActivity
import com.tapadoo.alerter.Alerter
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var loginViewModel: LoginViewModel
    private lateinit var user: UserModel
    private val validationHelper: ValidationHelper = ValidationHelper()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()
        binding.btnLogin.isEnabled = false

        alertFromRegisterActivity()
        focusListener()
        buttonListener()
        setupViewModel()
        setupAction()
    }

    private fun alertFromRegisterActivity() {
        val message = intent.getStringExtra(EXTRA_MESSAGE_ALERT)
        if(message != null){
            Alerter.create(this@LoginActivity)
                .setTitle(getString(R.string.success))
                .setText(message)
                .setBackgroundColorRes(R.color.teal_200)
                .setDuration(1500)
                .setIcon(R.drawable.ic_info_24)
                .show()
        }
    }

    private fun focusListener() {
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
        binding.tvRegister.setOnClickListener{
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }
    }

    private fun enableButton() {
        val validEmail = binding.layoutEmail.helperText == null
        val validPassword = binding.layoutPassword.helperText == null
        val textEmail = !binding.edtEmail.text.isNullOrBlank()
        val textPassword = !binding.edtPassword.text.isNullOrBlank()

        binding.btnLogin.isEnabled = (validEmail && validPassword && textEmail && textPassword)
    }

    private fun setupViewModel() {
        loginViewModel = ViewModelProvider(
            this, ViewModelFactory(UserPreference.getInstance(dataStore),this)
        )[LoginViewModel::class.java]

        loginViewModel.getUser().observe(this) { user -> this.user = user }
        loginViewModel.showLoading.observe(this) {showLoading(it)}
    }


    private fun setupAction(){
        binding.apply {
            btnLogin.setOnClickListener {
                val email = edtEmail.text.toString()
                val password = edtPassword.text.toString()

                val jsonObject = JSONObject()
                jsonObject.put(EMAIL, email)
                jsonObject.put(PASSWORD, password)
                val jsonObjectString = jsonObject.toString()
                val requestBody = jsonObjectString.toRequestBody("application/json".toMediaTypeOrNull())

                loginViewModel.loginUser(requestBody)


                loginViewModel.returnResponse.observe(this@LoginActivity){
                    if(it.status == 200){
                        val intent = Intent(this@LoginActivity, MainActivity::class.java)
                        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                        intent.putExtra(EXTRA_MESSAGE_ALERT,it.message)
                        startActivity(intent)
                        finish()
                    }else{
                        if(loginViewModel.showLoading.value == false){
                            Alerter.create(this@LoginActivity)
                                .setTitle(getString(R.string.failed_login))
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
        const val EMAIL = "email"
        const val PASSWORD = "password"
        const val EXTRA_MESSAGE_ALERT = "extra_message_alert"
    }
}

