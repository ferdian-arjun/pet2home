package com.capstone.pet2home.ui.settings.editprofile

import android.R.style.Theme_Holo_Light_Dialog_MinWidth
import android.app.DatePickerDialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.MenuItem
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import com.capstone.pet2home.R
import com.capstone.pet2home.databinding.ActivityEditProfileBinding
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
}