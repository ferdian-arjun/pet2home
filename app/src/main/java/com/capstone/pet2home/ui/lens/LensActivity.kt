package com.capstone.pet2home.ui.lens

import android.graphics.BitmapFactory
import android.os.Build
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import coil.load
import com.capstone.pet2home.R
import com.capstone.pet2home.databinding.ActivityLensBinding
import com.capstone.pet2home.rotateBitmap
import java.io.File

class LensActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLensBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val actionBar: ActionBar? = supportActionBar
        actionBar?.apply {
            setTitle(R.string.title_lens)
            setDisplayHomeAsUpEnabled(true)
            setHomeAsUpIndicator(R.drawable.ic_arrow_back_24)
        }
        binding = ActivityLensBinding.inflate(layoutInflater)
        setContentView(binding.root)
        showLoading(false)

        val myFile = intent?.getSerializableExtra("picture") as File
        val isBackCamera = intent.getBooleanExtra("isBackCamera", true)
        val result = rotateBitmap(
            BitmapFactory.decodeFile(myFile.path),
            isBackCamera
        )
        binding.previewImage.load(result){
            crossfade(true)
            crossfade(1000)
        }

        binding.btnBack.setOnClickListener{
            finish()
        }

        binding.btnUpload.setOnClickListener {
            showLoading(true)
        }
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBar.visibility = View.VISIBLE
            binding.tvProgress.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
            binding.tvProgress.visibility = View.GONE
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
            return true
        }
        return super.onContextItemSelected(item)
    }

    public override fun onResume() {
        super.onResume()
        hideSystemUI()
    }

    private fun hideSystemUI() {
        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
        supportActionBar?.hide()
    }
}