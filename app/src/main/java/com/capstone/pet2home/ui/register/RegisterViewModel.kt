package com.capstone.pet2home.ui.register

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.capstone.pet2home.api.ApiConfig
import com.capstone.pet2home.api.response.RegisterRes
import com.capstone.pet2home.helper.ReturnResponse
import com.capstone.pet2home.preference.UserPreference
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisterViewModel(private val pref: UserPreference) : ViewModel() {

    private val _showLoading = MutableLiveData<Boolean>()
    val showLoading: LiveData<Boolean> = _showLoading

    private val _returnResponse = MutableLiveData<ReturnResponse>()
    val returnResponse: LiveData<ReturnResponse> = _returnResponse

    fun registerUser(requestBody: RequestBody) {
        _showLoading.value = true
        val service = ApiConfig.getApiService().register(requestBody)
        service.enqueue(object : Callback<RegisterRes> {
            override fun onResponse(call: Call<RegisterRes>, response: Response<RegisterRes>) {
                val responseBody = response.body()
                if(responseBody != null){
                    _returnResponse.postValue(ReturnResponse(status = responseBody.status, message = responseBody.message))
                }else{
                    _returnResponse.postValue(ReturnResponse(status = response.code(), message = response.message()))
                }
                _showLoading.value = false
            }

            override fun onFailure(call: Call<RegisterRes>, t: Throwable) {
                _returnResponse.postValue(ReturnResponse(status = 500, message = t.message.toString()))
                _showLoading.value = false
            }
        })
    }
}