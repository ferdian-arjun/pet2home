package com.capstone.pet2home.ui.postdetail

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.capstone.pet2home.R

class PostDetailActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_post_detail)
        supportActionBar?.hide()
    }
}