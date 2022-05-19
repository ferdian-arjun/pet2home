package com.capstone.pet2home.ui.settings.editprofile

import android.Manifest
import android.R.style.Theme_Holo_Light_Dialog_MinWidth
import android.app.AlertDialog
import android.app.DatePickerDialog
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
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.capstone.pet2home.R
import com.capstone.pet2home.databinding.ActivityEditProfileBinding
import com.capstone.pet2home.rotateBitmap
import com.capstone.pet2home.ui.camera.CameraActivity
import com.capstone.pet2home.uriToFile
import java.io.File
import java.util.*

class EditProfileActivity : AppCompatActivity() {
    private lateinit var binding: ActivityEditProfileBinding
    private var itemsGender: Array<String> = arrayOf("Man","Woman")
    private lateinit var onDateSetListener: DatePickerDialog.OnDateSetListener


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

        viewSetupInputDropdownGender()
        viewSetupInputDatePicker()
        buttonListener()
    }

    private fun buttonListener(){
        binding.btnChangePhoto.setOnClickListener{
            chooseProfilPicture()
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

            binding.imageUserAvatar.setImageBitmap(result)
        }
    }

    private val launcherIntentGallery = registerForActivityResult(ActivityResultContracts.StartActivityForResult()
    ){
        result ->
        if (result.resultCode == RESULT_OK){
            val selectedImg: Uri = result.data?.data as Uri
            val myFile = uriToFile(selectedImg, this)
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
            val date = "$dayOfMonth/$month/$year"
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
                    Toast.makeText(applicationContext, "Item : $item",Toast.LENGTH_SHORT).show()
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

    companion object {
        const val CAMERA_X_RESULT = 200
        private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)
        private const val REQUEST_CODE_PERMISSIONS = 20
    }
}