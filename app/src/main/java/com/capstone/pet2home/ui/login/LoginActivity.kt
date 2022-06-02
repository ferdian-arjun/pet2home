package com.capstone.pet2home.ui.login

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import com.capstone.pet2home.MainActivity
import com.capstone.pet2home.databinding.ActivityLoginBinding
import com.capstone.pet2home.emailFormat
import com.capstone.pet2home.helper.isEmailNotValid
import com.capstone.pet2home.model.UserModel
import com.capstone.pet2home.preference.UserPreference
import com.capstone.pet2home.ui.ViewModelFactory
import com.capstone.pet2home.ui.register.RegisterActivity
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var loginViewModel: LoginViewModel
    private lateinit var user: UserModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setMyButtonEnable()
        buttonListener()
        setupViewModel()
        setupAction()
    }

    private fun buttonListener(){
        binding.tvRegister.setOnClickListener{
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }
    }

    private fun setMyButtonEnable(){
        binding.edtEmail.addTextChangedListener(onTextChanged = { _, _, _, _ ->
            enableButton()
        })
        binding.edtPassword.addTextChangedListener(onTextChanged = { _, _, _, _ ->
            enableButton()
        })
    }

    private fun enableButton() {
        val email = binding.edtEmail.text
        val password = binding.edtPassword.text

        binding.btnLogin.isEnabled =
            password != null && email != null && binding.edtPassword.text.toString().length >= 6 && emailFormat(
                binding.edtEmail.text.toString()
            )
    }

    private fun setupViewModel() {
        loginViewModel = ViewModelProvider(
            this, ViewModelFactory(UserPreference.getInstance(dataStore),this)
        )[LoginViewModel::class.java]

        loginViewModel.getUser().observe(this) { user -> this.user = user }
    }


    private fun setupAction(){
        binding.apply {
            btnLogin.setOnClickListener {
                val email = edtEmail.text.toString()
                val password = edtPassword.text.toString()

                if (email.isNotEmpty() && !email.isEmailNotValid() && password.isNotEmpty() && password.length > 5){
                    val jsonObject = JSONObject()
                    jsonObject.put(EMAIL, email)
                    jsonObject.put(PASSWORD, password)
                    val jsonObjectString = jsonObject.toString()
                    val requestBody = jsonObjectString.toRequestBody("application/json".toMediaTypeOrNull())

                    loginViewModel.loginUser(requestBody)

                    loginViewModel.returnResponse.observe(this@LoginActivity){
                        if(it.status == 200){
                            Toast.makeText(this@LoginActivity, it.message, Toast.LENGTH_SHORT).show()
                            val intent = Intent(this@LoginActivity, MainActivity::class.java)
                            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                            startActivity(intent)
                            finish()
                        }else{
                            AlertDialog.Builder(this@LoginActivity).apply {
                                setTitle(it.status)
                                setMessage(it.message)
                                create()
                                show()
                            }
                        }
                    }

                }
            }
        }
    }

    companion object {
        val EMAIL = "email"
        val PASSWORD = "password"

    }
}