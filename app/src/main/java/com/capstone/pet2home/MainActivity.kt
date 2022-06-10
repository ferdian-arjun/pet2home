package com.capstone.pet2home

import android.Manifest
import android.annotation.TargetApi
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.capstone.pet2home.databinding.ActivityMainBinding
import com.capstone.pet2home.helper.SettingsHelper
import com.capstone.pet2home.preference.UserPreference
import com.capstone.pet2home.ui.MainViewModel
import com.capstone.pet2home.ui.ViewModelFactory
import com.capstone.pet2home.ui.camera.LensCameraActivity
import com.capstone.pet2home.ui.login.LoginActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.tapadoo.alerter.Alerter

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val settingHelper = SettingsHelper(this)
    private lateinit var languageNow: String
    private lateinit var mainViewModel: MainViewModel

    companion object {
        const val EXTRA_ALERT = "extra_alert"
        private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA,Manifest.permission.ACCESS_FINE_LOCATION)
        private const val REQUEST_CODE_PERMISSIONS = 10
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (!allPermissionsGranted()) {
                Toast.makeText(
                    this,
                    "Tidak mendapatkan permission.",
                    Toast.LENGTH_SHORT
                ).show()
                finish()
            }
        }
    }

    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(baseContext, it) == PackageManager.PERMISSION_GRANTED
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        settingHelper.loadLocal()
        settingHelper.saveOnBoardPage(false)
        languageNow = settingHelper.getLanguageActive()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        alertFromLoginActivity()
        setupViewModel()

        if (!allPermissionsGranted()) {
            ActivityCompat.requestPermissions(
                this,
                REQUIRED_PERMISSIONS,
                REQUEST_CODE_PERMISSIONS
            )
        }

        val navView: BottomNavigationView = binding.navView

        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(setOf(
            R.id.navigation_home, R.id.navigation_profile))
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.navigation_home -> {
                    supportActionBar?.hide()
                }
                else -> supportActionBar?.show()
            }
        }

    }

    private fun alertFromLoginActivity() {
        val message = intent.getStringExtra(LoginActivity.EXTRA_MESSAGE_ALERT)
        val message2 = intent.getStringExtra(EXTRA_ALERT)
        if(message != null){
            Alerter.create(this)
                .setTitle(getString(R.string.success))
                .setText(message)
                .setBackgroundColorRes(R.color.teal_200)
                .setDuration(1500)
                .setIcon(R.drawable.ic_info_24)
                .show()
        }

        if(message2 != null){
            Alerter.create(this)
                .setTitle(getString(R.string.success))
                .setText(message2)
                .setBackgroundColorRes(R.color.teal_200)
                .setDuration(1500)
                .setIcon(R.drawable.ic_info_24)
                .show()
        }
    }

    private fun setupViewModel() {
        mainViewModel = ViewModelProvider(this,
            ViewModelFactory(UserPreference.getInstance(dataStore), this)
        )[MainViewModel::class.java]

        mainViewModel.getUser().observe(this) { user ->
            if (user.token.isEmpty()) {
                startActivity(Intent(this, LoginActivity::class.java))
                finish()
            }
        }
    }

    fun onLens(item: MenuItem) {
        startActivity(Intent(this, LensCameraActivity::class.java))
        overridePendingTransition(0, 0)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }

    override fun onResume() {
        super.onResume()
        if(languageNow != settingHelper.getLanguageActive()){
            recreate()
        }
    }

}