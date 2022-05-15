package com.capstone.pet2home.ui.lens

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class LensViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is lens Fragment"
    }
    val text: LiveData<String> = _text
}