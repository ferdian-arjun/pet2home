package com.capstone.pet2home.ui.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.widget.addTextChangedListener
import com.capstone.pet2home.databinding.ActivityLoginBinding
import com.capstone.pet2home.emailFormat
import com.capstone.pet2home.ui.register.RegisterActivity

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setMyButtonEnable()
        buttonListener()
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
}