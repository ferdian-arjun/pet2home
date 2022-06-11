package com.capstone.pet2home.ui.lens.resuklens

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.capstone.pet2home.databinding.ActivityResultLensBinding

class ResultLensActivity : AppCompatActivity() {
    private lateinit var binding: ActivityResultLensBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityResultLensBinding.inflate(layoutInflater)
        setContentView(binding.root)


    }
}