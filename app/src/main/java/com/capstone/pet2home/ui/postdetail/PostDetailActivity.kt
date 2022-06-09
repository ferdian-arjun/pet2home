package com.capstone.pet2home.ui.postdetail

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.WindowInsets
import android.view.WindowManager
import androidx.annotation.StringRes
import com.capstone.pet2home.R
import com.capstone.pet2home.databinding.ActivityLensBinding
import com.capstone.pet2home.databinding.ActivityPostDetailBinding

class PostDetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPostDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_post_detail)
        binding = ActivityPostDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        binding.btnBack.setOnClickListener{
            finish()
        }
    }

    companion object {
        const val TAG = "PostDetailActivity"
        const val EXTRA_ID_POST = "extra_id_post"
    }
}