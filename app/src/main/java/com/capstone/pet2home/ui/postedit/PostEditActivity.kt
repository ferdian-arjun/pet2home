package com.capstone.pet2home.ui.postedit

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.ContentValues
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
import com.bumptech.glide.Glide
import com.capstone.pet2home.*
import com.capstone.pet2home.api.response.DataItemPet
import com.capstone.pet2home.databinding.ActivityPostEditBinding
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

class PostEditActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPostEditBinding
    private lateinit var postEditViewModel: PostEditViewModel
    private var itemsGender: Array<String> = arrayOf("Dog","Cat")
    private var itemsAge: Array<String> = arrayOf("0-6 month","6 month - 1 years", "1 - 2 years", ">2 years")
    private val validationHelper: ValidationHelper= ValidationHelper()
    private var getFile: File? = null
    private var getLatLong: Array<String>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPostEditBinding.inflate(layoutInflater)
        val actionBar: ActionBar? = supportActionBar
        actionBar?.apply {
            setTitle(R.string.title_post_edit)
            setDisplayHomeAsUpEnabled(true)
            setHomeAsUpIndicator(R.drawable.ic_arrow_back_24)
        }
        setContentView(binding.root)

        Places.initialize(applicationContext, BuildConfig.MAPS_API_KEY)

        setupViewModel()
        focusListener()
        buttonListener()
    }

    private fun buttonListener(){
        binding.btnChangePhoto.setOnClickListener{
            chooseProfilPicture()
        }

        binding.btnUpdatePost.setOnClickListener{
            setupAction()
        }

        binding.edtLocation.setOnClickListener{

            // return after the user has made a selection.
            val fields = listOf(Place.Field.ADDRESS, Place.Field.LAT_LNG, Place.Field.NAME)

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
                        Log.i(ContentValues.TAG, "Place: ${place.address} LatLong: ${place.latLng}")
                        getLatLong = arrayOf(place.latLng.latitude.toString(),place.latLng.longitude.toString())
                    }
                }
                AutocompleteActivity.RESULT_ERROR -> {
                    data?.let {
                        val status = Autocomplete.getStatusFromIntent(data)
                        Toast.makeText(applicationContext, status.statusMessage, Toast.LENGTH_SHORT).show()
                        Log.i(ContentValues.TAG, status.statusMessage ?: "")
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

            getFile = myFile

            binding.apply {
                imagePost.visibility = View.VISIBLE
                imagePost.setImageBitmap(result)
                btnChangePhoto.text = getString(R.string.text_change_photo)
            }
        }
    }

    private val launcherIntentGallery = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){ result ->
        if (result.resultCode == RESULT_OK){
            val selectedImg: Uri = result.data?.data as Uri
            val myFile = uriToFile(selectedImg, this)

            getFile = myFile

            binding.apply {
                imagePost.visibility = View.VISIBLE
                imagePost.setImageURI(selectedImg)
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

        binding.autoCompleteUsia.addTextChangedListener {
            viewSetupInputDropdownAge()
        }

        binding.autoCompleteJenisHewan.addTextChangedListener {
            viewSetupInputDropdownGender()
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
            btnUpdatePost.isEnabled = nameHewan && jenisHewan && nomerWhatsapp && deskripsi && usia  && lokasi  && instagram && lat && lon
        }
    }

    private fun setupViewModel() {
        postEditViewModel = ViewModelProvider(
            this, ViewModelFactory(UserPreference.getInstance(dataStore),this)
        )[PostEditViewModel::class.java]

        postEditViewModel.getUser().observe(this) {
            val getPetId = intent.getStringExtra(EXTRA_ID_POST)
            if(getPetId != null) {
                postEditViewModel.getPetApi(getPetId, it.token)
            }
        }

        postEditViewModel.dataPost.observe(this){
            if(it != null) setDataForm(it)
        }


        postEditViewModel.showLoading.observe(this) {showLoading(it)}
    }

    private fun setDataForm(data: DataItemPet) {
        binding.apply {
            Glide.with(imagePost).load(URL_IMAGE + data.pic).into(imagePost)
            edtNamaHewan.setText(data.title)
            autoCompleteJenisHewan.setText(data.breed)
            autoCompleteUsia.setText(data.age)
            edtLocation.setText(data.location)
            edtInstagram.setText(data.insta)
            edtWhatshapp.setText(data.whatsapp)
            edtDeskripsi.setText(data.description)
            getLatLong = arrayOf(data.lat, data.lon)
            layoutLocation.hint = "${getString(R.string.text_post_location)} (${data.lat} | ${data.lon})"
        }
    }

    private fun setupAction() {
        binding.apply {
            val getPetId = intent.getStringExtra(EXTRA_ID_POST)
            if(getPetId != null){
                postEditViewModel.getUser().observe(this@PostEditActivity){
                    //form fields
                    val description = edtDeskripsi.text.toString().toRequestBody("text/plain".toMediaType())
                    val instagram = edtInstagram.text.toString().toRequestBody("text/plain".toMediaType())
                    val age = autoCompleteUsia.text.toString().toRequestBody("text/plain".toMediaType())
                    val idUser = it.userId.toRequestBody("text/plain".toMediaType())
                    val title = edtNamaHewan.text.toString().toRequestBody("text/plain".toMediaType())
                    val breed = autoCompleteJenisHewan.text.toString().toRequestBody("text/plain".toMediaType())
                    val location = edtLocation.text.toString().toRequestBody("text/plain".toMediaType())
                    val whatsApp = edtWhatshapp.text.toString().toRequestBody("text/plain".toMediaType())
                    val latPost = getLatLong?.get(0)
                    val lonPost = getLatLong?.get(1)

                    val lat = latPost.toString().toRequestBody("text/plain".toMediaType())
                    val lon = lonPost.toString().toRequestBody("text/plain".toMediaType())
                    val data = arrayOf<RequestBody>(description,instagram,age,idUser,title,breed,location,whatsApp,lat,lon)
                    if(getFile != null){
                        val file = reduceFileImage(getFile as File)
                        val requestImageFile = file.asRequestBody("image/jpeg".toMediaTypeOrNull())
                        val imageMultipart: MultipartBody.Part = MultipartBody.Part.createFormData(
                            "image","post_" + it.userId + file.name,requestImageFile
                        )
                        postEditViewModel.updatePost(idPost = getPetId, token = it.token, image = imageMultipart, data = data)
                    }else{
                        postEditViewModel.updatePost(idPost = getPetId, token = it.token, data = data)
                    }
                }

                postEditViewModel.returnResponse.observe(this@PostEditActivity){
                    if(it.status == 200){
                        alert(true, it.message)
                    }else{
                        if(postEditViewModel.showLoading.value == false){
                            alert(false, it.message)
                        }
                    }

                    edtNamaHewan.requestFocus()
                }
            }
        }
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

    private fun alert(success: Boolean, message: String) {
        if(success){
            Alerter.create(this)
                .setTitle(getString(R.string.success))
                .setText(getString(R.string.successfull_update))
                .setBackgroundColorRes(R.color.teal_200)
                .setDuration(1500)
                .setIcon(R.drawable.ic_info_24)
                .show()
        }else{
            Alerter.create(this)
                .setTitle(getString(R.string.failed))
                .setText(message)
                .setBackgroundColorRes(R.color.red)
                .setDuration(1500)
                .setIcon(R.drawable.ic_error)
                .show()
        }
    }

    companion object {
        const val CAMERA_X_RESULT = 200
        private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)
        private const val REQUEST_CODE_PERMISSIONS = 20
        const val TAG = "PostEditActivity"
        const val EXTRA_ID_POST = "extra_id_post"
        const val URL_IMAGE = BuildConfig.BASE_URL + "public/upload/"
        const val LAT = "lat"
        const val LON = "lon"
    }
}