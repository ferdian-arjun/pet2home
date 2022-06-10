package com.capstone.pet2home.ui.postdetail

import android.icu.util.TimeZone
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.WindowInsets
import android.view.WindowManager
import androidx.annotation.RequiresApi
import com.bumptech.glide.Glide
import com.capstone.pet2home.R
import com.capstone.pet2home.databinding.ActivityLensBinding
import com.capstone.pet2home.databinding.ActivityPostDetailBinding
import com.capstone.pet2home.formatDate
import com.capstone.pet2home.model.Post

class PostDetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPostDetailBinding

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_post_detail)
        binding = ActivityPostDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        val post = intent.getParcelableExtra<Post>(DETAIL_POST) as Post
        Glide.with(this)
            .load(post.pic)
            .into(binding.ivImagePost)
        binding.tvCreateDatePost.text = formatDate(post.createdAt, java.util.TimeZone.getDefault().id)
        binding.tvTitlePost.text = post.tittle
        binding.tvLocationPost.text = post.location
        binding.tvType.text = post.breed
        binding.tvAge.text = post.age
        binding.tvAboutPost.text = post.description


        binding.btnBack.setOnClickListener{
            finish()
        }
    }

    companion object{
        const val DETAIL_POST = "detail_post"

    }
}