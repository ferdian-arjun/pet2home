package com.capstone.pet2home.ui.register

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
import com.capstone.pet2home.databinding.ActivityRegisterBinding
import com.capstone.pet2home.emailFormat
import com.capstone.pet2home.helper.isEmailNotValid
import com.capstone.pet2home.preference.UserPreference
import com.capstone.pet2home.ui.ViewModelFactory
import com.capstone.pet2home.ui.login.LoginActivity
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding
    private lateinit var registerViewModel: RegisterViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setMyButtonEnable()
        buttonListener()
        setupViewModel()
        setupAction()
    }

    private fun buttonListener(){
        binding.tvLogin.setOnClickListener{
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
    }

    private fun setMyButtonEnable() {
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

        binding.btnRegister.isEnabled =
            password != null && email != null && binding.edtPassword.text.toString().length >= 6 && emailFormat(
                binding.edtEmail.text.toString()
            )
    }

    private fun setupViewModel() {
        registerViewModel = ViewModelProvider(
            this, ViewModelFactory(UserPreference.getInstance(dataStore),this)
        )[RegisterViewModel::class.java]
    }

    private fun setupAction() {
        binding.apply {
            btnRegister.setOnClickListener {
                val fullName = edtName.text.toString()
                val email = edtEmail.text.toString()
                val password = edtPassword.text.toString()

                if (fullName.isNotEmpty() && email.isNotEmpty() && !email.isEmailNotValid() && password.isNotEmpty() && password.length > 5){
                    val jsonObject = JSONObject()
                    jsonObject.put(FULL_NAME, fullName)
                    jsonObject.put(EMAIL, email)
                    jsonObject.put(PASSWORD, password)
                    val jsonObjectString = jsonObject.toString()
                    val requestBody = jsonObjectString.toRequestBody("application/json".toMediaTypeOrNull())

                    registerViewModel.registerUser(requestBody)

                    registerViewModel.returnResponse.observe(this@RegisterActivity){
                        if(it.status == 201){
                            Toast.makeText(this@RegisterActivity, it.message, Toast.LENGTH_SHORT).show()
                            val intent = Intent(this@RegisterActivity, LoginActivity::class.java)
                            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                            startActivity(intent)
                            finish()
                        }else{
                            AlertDialog.Builder(this@RegisterActivity).apply {
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
        private const val FULL_NAME = "fullname"
        private const val EMAIL = "email"
        private const val PASSWORD = "password"

    }
}