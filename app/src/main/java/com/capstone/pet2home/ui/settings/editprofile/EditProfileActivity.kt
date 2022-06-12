package com.capstone.pet2home.ui.settings.editprofile

import android.Manifest
import android.R.style.Theme_Holo_Light_Dialog_MinWidth
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.content.Intent.ACTION_GET_CONTENT
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.WindowManager
import android.widget.*
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
import com.capstone.pet2home.R
import com.capstone.pet2home.api.response.GetUserRes
import com.capstone.pet2home.databinding.ActivityEditProfileBinding
import com.capstone.pet2home.helper.ValidationHelper
import com.capstone.pet2home.preference.UserPreference
import com.capstone.pet2home.reduceFileImage
import com.capstone.pet2home.rotateBitmap
import com.capstone.pet2home.ui.ViewModelFactory
import com.capstone.pet2home.ui.camera.CameraActivity
import com.capstone.pet2home.ui.profile.ProfileFragment
import com.capstone.pet2home.uriToFile
import com.tapadoo.alerter.Alerter
import es.dmoral.toasty.Toasty
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import java.util.*

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class EditProfileActivity : AppCompatActivity() {
    private lateinit var binding: ActivityEditProfileBinding
    private var itemsGender: Array<String> = arrayOf("man","woman")
    private lateinit var onDateSetListener: DatePickerDialog.OnDateSetListener
    private lateinit var editProfileViewModel: EditProfileViewModel
    private val validationHelper: ValidationHelper = ValidationHelper()
    private var getFile: File? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val actionBar: ActionBar? = supportActionBar
        actionBar?.apply {
            setTitle(R.string.title_edit_profile)
            setDisplayHomeAsUpEnabled(true)
            setHomeAsUpIndicator(R.drawable.ic_arrow_back_24)
        }
        setContentView(R.layout.activity_edit_profile)

        binding = ActivityEditProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        focusListener()
        buttonListener()
        setupViewModel()
        setupAction()

    }

    private fun setupViewModel() {
        editProfileViewModel = ViewModelProvider(
            this, ViewModelFactory(UserPreference.getInstance(dataStore),this)
        )[EditProfileViewModel::class.java]

        editProfileViewModel.getUser().observe(this) {
            editProfileViewModel.getUserApi(it.userId, it.token)
        }

        editProfileViewModel.usersData.observe(this){userData ->
            if(userData != null) setDataForm(userData)
        }

        editProfileViewModel.returnResponse.observe(this){
            if(it.status != 200){
                Toasty.error(this,it.message, Toast.LENGTH_SHORT, true).show()
            }
        }

        editProfileViewModel.showLoading.observe(this) {showLoading(it)}
    }

    private fun setDataForm(user: GetUserRes) {
        binding.apply {
            Glide.with(this@EditProfileActivity).load(ProfileFragment.URL_AVATAR + user.result.data[0]?.avatar).circleCrop().into(imageUserAvatar)
            edtFullName.setText(user.result.data[0]?.username)
            edtEmail.setText(user.result.data[0]?.email)
            edtTelp.setText(user.result.data[0]?.phoneNumber)
            edtDate.setText(user.result.data[0]?.birthDate?.substringBefore("T"))
            autoCompleteGender.setText(user.result.data[0]?.gender)
        }
    }

    private fun focusListener() {
        binding.edtFullName.addTextChangedListener {
            binding.layoutFullName.helperText = validationHelper.validFullName(binding.edtFullName.text.toString())
            enableButton()
        }

        binding.edtEmail.addTextChangedListener {
            binding.layoutEmail.helperText = validationHelper.validEmail(binding.edtEmail.text.toString())
            enableButton()
        }

        binding.edtTelp.addTextChangedListener {
            binding.layoutTelp.helperText = validationHelper.validPhoneNumber(binding.edtTelp.text.toString())
            enableButton()
        }

        binding.autoCompleteGender.addTextChangedListener{
            viewSetupInputDropdownGender()
        }

        binding.edtDate.addTextChangedListener{
            viewSetupInputDatePicker()
        }

    }

    private fun enableButton() {
        val validEmail = binding.layoutEmail.helperText == null
        val validFullName = binding.layoutFullName.helperText == null

        val textEmail = !binding.edtEmail.text.isNullOrBlank()
        val textFullName = !binding.edtFullName.text.isNullOrBlank()

        binding.btnUpdate.isEnabled = (validEmail && validFullName && textEmail && textFullName)
    }

    private fun setupAction() {
        binding.apply {
            btnUpdate.setOnClickListener {
                val fullName = edtFullName.text.toString().toRequestBody("text/plain".toMediaType())
                val email = edtEmail.text.toString().toRequestBody("text/plain".toMediaType())
                val phoneNumber = edtTelp.text.toString().toRequestBody("text/plain".toMediaType())
                val birthDate = edtDate.text.toString().toRequestBody("text/plain".toMediaType())
                val gender = autoCompleteGender.text.toString().toRequestBody("text/plain".toMediaType())

                val data = arrayOf<RequestBody>(fullName,email,phoneNumber,birthDate,gender)
                if(getFile != null){
                    editProfileViewModel.getUser().observe(this@EditProfileActivity) {
                        val file = reduceFileImage(getFile as File)
                        val requestImageFile = file.asRequestBody("image/jpeg".toMediaTypeOrNull())
                        val imageMultipart: MultipartBody.Part = MultipartBody.Part.createFormData(
                            "avatar",it.userId + file.name,requestImageFile
                        )
                        editProfileViewModel.sendData(userId = it.userId, token = it.token, imageMultipart = imageMultipart, data = data)
                    }
                }else{
                    editProfileViewModel.getUser().observe(this@EditProfileActivity) {
                        editProfileViewModel.sendData(userId = it.userId,token = it.token,data = data)
                    }
                }

                editProfileViewModel.returnResponse.observe(this@EditProfileActivity){
                    if(it.status == 200){
                        alert(true, it.message)
                    }else{
                        alert(false, it.message)
                    }

                    edtFullName.requestFocus()
                }
            }
        }
    }

    private fun buttonListener(){
        binding.btnChangePhoto.setOnClickListener{
            chooseProfilePicture()
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
            intent.action = ACTION_GET_CONTENT
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
            binding.imageUserAvatar.setImageBitmap(result)
        }
    }

    private val launcherIntentGallery = registerForActivityResult(ActivityResultContracts.StartActivityForResult()
    ){
        result ->
        if (result.resultCode == RESULT_OK){
            val selectedImg: Uri = result.data?.data as Uri
            val myFile = uriToFile(selectedImg, this)
            getFile = myFile
            binding.imageUserAvatar.setImageURI(selectedImg)
        }
    }

    private fun viewSetupInputDatePicker() {
        binding.edtDate.showSoftInputOnFocus = false

        val calendar: Calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        binding.edtDate.setOnClickListener {
            binding.edtDate.showSoftInputOnFocus = false
            val datePickerDialog = DatePickerDialog(
                this@EditProfileActivity, Theme_Holo_Light_Dialog_MinWidth,
                onDateSetListener,year , month, day)
            datePickerDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            datePickerDialog.show()
        }

        onDateSetListener = DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
            val month: Int = month + 1
            val date = "$year-$month-$dayOfMonth"
            binding.edtDate.setText(date)
        }
    }

    private fun viewSetupInputDropdownGender() {
        val adapterItems: ArrayAdapter<String> = ArrayAdapter(this,R.layout.list_item_gender,itemsGender)
        binding.apply {
            autoCompleteGender.setAdapter(adapterItems)
            autoCompleteGender.onItemClickListener =
                AdapterView.OnItemClickListener { parent, view, position, id ->
                    val item: String = parent.getItemAtPosition(position).toString()
//                    Toast.makeText(applicationContext, "Item : $item",Toast.LENGTH_SHORT).show()
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
                ActivityCompat.requestPermissions(this, REQUIRED_PERMISSIONS, REQUEST_CODE_PERMISSIONS)
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

    private fun alert(success: Boolean, message: String,) {
        if(success){
            Alerter.create(this)
                .setTitle(getString(R.string.success))
                .setText(message)
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
        const val FULL_NAME = "username"
        const val EMAIL = "email"
        const val BIRTH_DATE = "birth_date"
        const val BIRTH_PLACE = "birth_place"
        const val GENDER = "gender"
        const val PHONE_NUMBER = "phone_number"
    }
}