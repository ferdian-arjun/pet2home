package com.capstone.pet2home.ui.register

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import com.capstone.pet2home.databinding.ActivityRegisterBinding
import com.capstone.pet2home.emailFormat
import com.capstone.pet2home.ui.login.LoginActivity

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setMyButtonEnable()
        buttonListener()
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
}