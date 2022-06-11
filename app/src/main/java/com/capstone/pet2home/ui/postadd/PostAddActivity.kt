package com.capstone.pet2home.ui.postadd

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.ContentValues.TAG
import android.content.Context
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
import android.view.WindowManager
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.widget.addTextChangedListener
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import com.capstone.pet2home.*
import com.capstone.pet2home.databinding.ActivityPostAddBinding
import com.capstone.pet2home.helper.ValidationHelper
import com.capstone.pet2home.preference.UserPreference
import com.capstone.pet2home.ui.ViewModelFactory
import com.capstone.pet2home.ui.camera.CameraActivity
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.widget.Autocomplete
import com.google.android.libraries.places.widget.AutocompleteActivity
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode
import com.tapadoo.alerter.Alerter
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class PostAddActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPostAddBinding
    private lateinit var postAddViewModel: PostAddViewModel
    private var itemsGender: Array<String> = arrayOf("Dog","Cat")
    private var itemsAge: Array<String> = arrayOf("0-6 month","6 month - 1 years", "1 - 2 years", ">2 years")
    private val validationHelper: ValidationHelper = ValidationHelper()
    private var getFile: File? = null
    private var getLatLong: Array<String>? = null

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

        setupViewModel()
        focusListener()
        viewSetupInputDropdownGender()
        viewSetupInputDropdownAge()
        buttonListener()
        setupAction()
    }

    private fun buttonListener(){
        binding.btnChangePhoto.setOnClickListener{
            chooseProfilePicture()
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
                        getLatLong = arrayOf(place.latLng.latitude.toString(),place.latLng.longitude.toString())
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

    private fun chooseProfilePicture(){

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

            getFile = myFile

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

            getFile = myFile

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

    private fun focusListener(){

        binding.edtNamaHewan.addTextChangedListener{
            binding.layoutNamaHewan.helperText = validationHelper.validMaxChar(binding.edtNamaHewan.text.toString())
            enableButton()
        }

        binding.edtWhatshapp.addTextChangedListener{
            binding.layoutWhatsapp.helperText = validationHelper.validPhoneNumber(binding.edtWhatshapp.text.toString())
            enableButton()
        }

        binding.edtInstagram.addTextChangedListener{
            binding.layoutInstagram.helperText = validationHelper.validInstagram(binding.edtInstagram.text.toString())
            enableButton()
        }

        binding.edtLocation.addTextChangedListener{
            enableButton()
        }

        binding.edtDeskripsi.addTextChangedListener{
            enableButton()
        }

    }

    private fun enableButton() {
        binding.apply {
            val nameHewan = !edtNamaHewan.text.isNullOrBlank() && layoutNamaHewan.helperText == null
            val jenisHewan = !autoCompleteJenisHewan.text.isNullOrBlank()
            val usia = !autoCompleteUsia.text.isNullOrBlank()
            val lokasi = !edtLocation.text .isNullOrBlank() && layoutLocation.helperText == null
            val instagram = !edtInstagram.text.isNullOrBlank() && layoutInstagram.helperText == null
            val nomerWhatsapp = !edtWhatshapp.text.isNullOrBlank() && layoutWhatsapp.helperText == null
            val deskripsi = !edtDeskripsi.text .isNullOrBlank() && layoutDeskripsi.helperText == null
            val lat = !getLatLong?.get(0).isNullOrBlank()
            val lon = !getLatLong?.get(1).isNullOrBlank()

            layoutLocation.hint = "${getString(R.string.text_post_location)} (${getLatLong?.get(0)} | ${getLatLong?.get(1)})"
            btnCreatePost.isEnabled = nameHewan && jenisHewan && nomerWhatsapp && deskripsi && usia  && lokasi  && instagram && lat && lon &&  getFile != null
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
            return true
        }
        return super.onContextItemSelected(item)
    }

    private fun setupViewModel() {
        postAddViewModel = ViewModelProvider(
            this, ViewModelFactory(UserPreference.getInstance(dataStore),this)
        )[PostAddViewModel::class.java]

        postAddViewModel.showLoading.observe(this) {showLoading(it)}
    }

    private fun setupAction() {
        binding.apply {
            btnCreatePost.setOnClickListener {
                postAddViewModel.getUser().observe(this@PostAddActivity){
                    //form fields
                    val description = edtDeskripsi.text.toString().toRequestBody("text/plain".toMediaType())
                    val instagram = edtInstagram.text.toString().toRequestBody("text/plain".toMediaType())
                    val age = autoCompleteUsia.text.toString().toRequestBody("text/plain".toMediaType())
                    val idUser = it.userId.toRequestBody("text/plain".toMediaType())
                    val title = edtNamaHewan.text.toString().toRequestBody("text/plain".toMediaType())
                    val breed = autoCompleteJenisHewan.text.toString().toRequestBody("text/plain".toMediaType())
                    val location = edtLocation.text.toString().toRequestBody("text/plain".toMediaType())
                    val whatsApp = edtWhatshapp.text.toString().toRequestBody("text/plain".toMediaType())
                    val lat = getLatLong?.get(0).toString().toRequestBody("text/plain".toMediaType())
                    val lon = getLatLong?.get(1).toString().toRequestBody("text/plain".toMediaType())
                    //files
                    val file = reduceFileImage(getFile as File)
                    val requestImageFile = file.asRequestBody("image/jpeg".toMediaTypeOrNull())
                    val imageMultipart: MultipartBody.Part = MultipartBody.Part.createFormData(
                        "image","post_" + it.userId + file.name,requestImageFile
                    )

                    val data = arrayOf<RequestBody>(description,instagram,age,idUser,title,breed,location,whatsApp,lat,lon)

                    postAddViewModel.createPost(token = it.token, image = imageMultipart, data = data)
                }

                postAddViewModel.returnResponse.observe(this@PostAddActivity){
                    if(it.status == 200){
                        val intent = Intent(this@PostAddActivity, MainActivity::class.java)
                        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                        intent.putExtra(MainActivity.EXTRA_ALERT,it.message)
                        startActivity(intent)
                        finish()
                    }else{
                        if(postAddViewModel.showLoading.value == false){
                            Alerter.create(this@PostAddActivity)
                                .setTitle(getString(R.string.failed))
                                .setText(it.message)
                                .setBackgroundColorRes(R.color.red)
                                .setDuration(1500)
                                .setIcon(R.drawable.ic_error)
                                .show()
                        }

                        edtNamaHewan.requestFocus()
                    }
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        enableButton()
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

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBar.visibility = View.VISIBLE
            binding.viewLoading.visibility = View.VISIBLE
            window.setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
        } else {
            binding.progressBar.visibility = View.GONE
            binding.viewLoading.visibility = View.GONE
            window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
        }
    }


    companion object {
        const val CAMERA_X_RESULT = 200
        private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)
        private const val REQUEST_CODE_PERMISSIONS = 20
        const val DESCRIPTION = "description"
        const val INSTAGRAM = "insta"
        const val AGE = "age"
        const val ID_USER = "id_user"
        const val TITLE = "title"
        const val BREED = "breed"
        const val LOCATION = "location"
        const val WHATSAPP = "whatsapp"
        const val LAT = "lat"
        const val LON = "lon"
    }
}