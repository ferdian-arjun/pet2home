package com.capstone.pet2home.ui.lens

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.capstone.pet2home.api.ApiConfig
import com.capstone.pet2home.api.response.DataItemPet
import com.capstone.pet2home.api.response.PredictPetRes
import com.capstone.pet2home.api.response.StandardRes
import com.capstone.pet2home.helper.ReturnResponse
import com.capstone.pet2home.preference.UserPreference
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response



class LensViewModel(private val pref: UserPreference) : ViewModel()  {
    private val _showLoading = MutableLiveData<Boolean>()
    val showLoading: LiveData<Boolean> = _showLoading

    private val _returnResponse = MutableLiveData<ReturnResponse>()
    val returnResponse: LiveData<ReturnResponse> = _returnResponse

    private val _dataPredict = MutableLiveData<PredictPetRes>()
    val dataPredict: LiveData<PredictPetRes> = _dataPredict

    fun getPredictPet(image: MultipartBody.Part) {
        _showLoading.value = true
        val service = ApiConfig.getApiServiceML().getPredictPet(file = image)
        service.enqueue(object : Callback<PredictPetRes> {
            override fun onResponse(call: Call<PredictPetRes>, response: Response<PredictPetRes>) {
                val responses = response
                if(response.code() == 400){
                    _returnResponse.postValue(ReturnResponse(status = response.code(), message = response.message()))
                }else{
                    val responseBody = response.body()
                    if(responseBody?.error == null){
                        _dataPredict.postValue(responseBody)
                        _returnResponse.postValue(ReturnResponse(status = response.hashCode(), message = response.message()))
                    }else{
                        _returnResponse.postValue(ReturnResponse(status = response.hashCode(), message = response.body()?.error.toString()))
                    }
                }

                _showLoading.value = false
            }

            override fun onFailure(call: Call<PredictPetRes>, t: Throwable) {
                _returnResponse.postValue(ReturnResponse(status = 500, message = t.message.toString()))
                _showLoading.value = false
            }
        })
    }
}