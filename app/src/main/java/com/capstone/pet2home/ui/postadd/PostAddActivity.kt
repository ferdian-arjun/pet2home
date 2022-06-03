package com.capstone.pet2home.ui.postadd

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.ContentValues.TAG
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.widget.addTextChangedListener
import com.capstone.pet2home.BuildConfig
import com.capstone.pet2home.R
import com.capstone.pet2home.databinding.ActivityPostAddBinding
import com.capstone.pet2home.rotateBitmap
import com.capstone.pet2home.ui.camera.CameraActivity
import com.capstone.pet2home.uriToFile
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.widget.Autocomplete
import com.google.android.libraries.places.widget.AutocompleteActivity
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode
import java.io.File

class PostAddActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPostAddBinding
    private var itemsGender: Array<String> = arrayOf("Dog","Cat")
    private var itemsAge: Array<String> = arrayOf("0-6 month","6 month - 1 years", "1 - 2 years", ">2 years")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPostAddBinding.inflate(layoutInflater)
        val actionBar: ActionBar? = supportActionBar
        actionBar?.apply {
            setTitle(R.string.title_post_add)
            setDisplayHomeAsUpEnabled(true)
            setHomeAsUpIndicator(R.drawable.ic_arrow_back_24)
        }
        setContentView(binding.root)

        Places.initialize(applicationContext, BuildConfig.MAPS_API_KEY)

        setMyButtonEnable()
        viewSetupInputDropdownGender()
        viewSetupInputDropdownAge()
        buttonListener()
    }

    private fun buttonListener(){
        binding.btnChangePhoto.setOnClickListener{
            chooseProfilPicture()
        }

        binding.btnPosting.setOnClickListener{

        }
        binding.edtLocation.setOnClickListener{

            // return after the user has made a selection.
            val fields = listOf(Place.Field.ADDRESS,Place.Field.LAT_LNG, Place.Field.NAME)

            // Start the autocomplete intent.
            val intent = Autocomplete.IntentBuilder(AutocompleteActivityMode.FULLSCREEN, fields)
                .build(this)
            startActivityForResult(intent, 100)


        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 100) {
            when (resultCode) {
                Activity.RESULT_OK -> {
                    data?.let {
                        val place = Autocomplete.getPlaceFromIntent(data)
                        binding.edtLocation.setText(place.address)
                        Log.i(TAG, "Place: ${place.address} LatLong: ${place.latLng}")
                    }
                }
                AutocompleteActivity.RESULT_ERROR -> {
                    data?.let {
                        val status = Autocomplete.getStatusFromIntent(data)
                        Toast.makeText(applicationContext, status.statusMessage, Toast.LENGTH_SHORT).show()
                        Log.i(TAG, status.statusMessage ?: "")
                    }
                }
                Activity.RESULT_CANCELED -> {
                    // The user canceled the operation.
                    Toast.makeText(applicationContext, "cancelled", Toast.LENGTH_SHORT).show()

                }
            }
            return
        }
    }

    private fun chooseProfilPicture(){

        val builder: AlertDialog.Builder = AlertDialog.Builder(this)
        val inflater: LayoutInflater = layoutInflater
        val dialogView: View = inflater.inflate(R.layout.alert_dialog_picture, null)
        builder.setCancelable(true)
        builder.setView(dialogView)

        val btnCamera: ImageView = dialogView.findViewById(R.id.btnCamera)
        val btnGalery: ImageView = dialogView.findViewById(R.id.btnGallery)

        val alertDialogPicture: AlertDialog = builder.create()
        alertDialogPicture.show()

        btnCamera.setOnClickListener{
            if(checkAndRequestPermission()){
                val intent = Intent(this, CameraActivity::class.java)
                launcherIntentCameraX.launch(intent)
            }
        }

        btnGalery.setOnClickListener{
            val intent = Intent()
            intent.action = Intent.ACTION_GET_CONTENT
            intent.type = "image/*"
            val chooser = Intent.createChooser(intent, "Choose a Picture")
            launcherIntentGallery.launch(chooser)
        }
    }

    private val launcherIntentCameraX = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
        if (it.resultCode == CAMERA_X_RESULT){
            val myFile = it.data?.getSerializableExtra("picture") as File
            val isBackCamera = it.data?.getBooleanExtra("isBackCamera", true) as Boolean

            val result = rotateBitmap(BitmapFactory.decodeFile(myFile.path), isBackCamera)

            binding.apply {
                imageUserAvatar.visibility = View.VISIBLE
                imageUserAvatar.setImageBitmap(result)
                btnChangePhoto.text = getString(R.string.text_change_photo)
            }
        }
    }

    private val launcherIntentGallery = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ){
            result ->
        if (result.resultCode == RESULT_OK){
            val selectedImg: Uri = result.data?.data as Uri
            val myFile = uriToFile(selectedImg, this)
            binding.apply {
                imageUserAvatar.visibility = View.VISIBLE
                imageUserAvatar.setImageURI(selectedImg)
                btnChangePhoto.text = getString(R.string.text_change_photo)
            }
        }
    }

    private fun viewSetupInputDropdownGender() {
        val adapterItems: ArrayAdapter<String> = ArrayAdapter(this,R.layout.list_item_gender,itemsGender)
        binding.apply {
            autoCompleteJenisHewan.setAdapter(adapterItems)
            autoCompleteJenisHewan.onItemClickListener =
                AdapterView.OnItemClickListener { parent, _, position, _ ->
                    val item: String = parent.getItemAtPosition(position).toString()
                    Toast.makeText(applicationContext, "Item : $item",Toast.LENGTH_SHORT).show()
                }
        }
    }

    private fun viewSetupInputDropdownAge() {
        val adapterItems: ArrayAdapter<String> = ArrayAdapter(this,R.layout.list_item_age,itemsAge)
        binding.apply {
            autoCompleteUsia.setAdapter(adapterItems)
            autoCompleteUsia.onItemClickListener =
                AdapterView.OnItemClickListener { parent, _, position, _ ->
                    val item: String = parent.getItemAtPosition(position).toString()
                    Toast.makeText(applicationContext, "Item : $item",Toast.LENGTH_SHORT).show()
                }
        }
    }

    private fun setMyButtonEnable(){
        binding.edtNamaHewan.addTextChangedListener(onTextChanged = { _, _, _, _ ->
            enableButton()
        })
        binding.autoCompleteJenisHewan.addTextChangedListener(onTextChanged = { _, _, _, _ ->
            enableButton()
        })
        binding.autoCompleteUsia.addTextChangedListener(onTextChanged = { _, _, _, _ ->
            enableButton()
        })
        binding.edtWhatshapp.addTextChangedListener(onTextChanged = {_, _, _, _ ->
            enableButton()
        })
        binding.edtInstagram.addTextChangedListener(onTextChanged = {_, _, _, _ ->
            enableButton()
        })
        binding.edtLocation.addTextChangedListener(onTextChanged = {_, _, _, _ ->
            enableButton()
        })
        binding.edtDeskripsi.addTextChangedListener(onTextChanged = {_, _, _, _ ->
            enableButton()
        })
    }

    private fun enableButton() {
        val nameHewan = binding.edtNamaHewan.text
        val jenisHewan = binding.autoCompleteJenisHewan.text
        val usia = binding.autoCompleteUsia.text
        val lokasi = binding.edtLocation.text
        val instagram = binding.edtInstagram.text
        val nomerWhatsapp = binding.edtWhatshapp.text
        val deskripsi = binding.edtWhatshapp.text


        binding.btnPosting.isEnabled =
            nameHewan != null && jenisHewan != null && nomerWhatsapp != null && deskripsi != null && usia != null && lokasi != null && instagram != null &&
                    binding.edtNamaHewan.text.toString().length >= 6 && binding.edtWhatshapp.text.toString().length >= 10 && binding.edtDeskripsi.text.toString().length >= 6

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
            return true
        }
        return super.onContextItemSelected(item)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_PERMISSIONS && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            val intent = Intent(this, CameraActivity::class.java)
            startActivity(intent)

            Toast.makeText(this, "mendapatkan permission.", Toast.LENGTH_SHORT).show()
        }else{
            Toast.makeText(this, "Tidak mendapatkan permission.", Toast.LENGTH_SHORT).show()
        }
    }

    private fun checkAndRequestPermission(): Boolean{
        if (Build.VERSION.SDK_INT >= 23){
            val cameraPermission: Int = ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
            if(cameraPermission == PackageManager.PERMISSION_DENIED){
                ActivityCompat.requestPermissions(this,
                    REQUIRED_PERMISSIONS,
                    REQUEST_CODE_PERMISSIONS
                )
                return false
            }
        }
        return true
    }

    companion object {
        const val CAMERA_X_RESULT = 200
        private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)
        private const val REQUEST_CODE_PERMISSIONS = 20
    }
}