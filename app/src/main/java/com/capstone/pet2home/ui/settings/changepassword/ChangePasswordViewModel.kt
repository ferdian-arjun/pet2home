package com.capstone.pet2home.ui.settings.changepassword

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.capstone.pet2home.api.ApiConfig
import com.capstone.pet2home.api.response.StandardRes
import com.capstone.pet2home.helper.ReturnResponse
import com.capstone.pet2home.model.UserModel
import com.capstone.pet2home.preference.UserPreference
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ChangePasswordViewModel(private val pref: UserPreference) : ViewModel() {

    private val _showLoading = MutableLiveData<Boolean>()
    val showLoading: LiveData<Boolean> = _showLoading

    private val _returnResponse = MutableLiveData<ReturnResponse>()
    val returnResponse: LiveData<ReturnResponse> = _returnResponse

    fun getUser(): LiveData<UserModel> {
        return pref.getUser().asLiveData()
    }

    fun sendData(user_id: String, token: String, requestBody: RequestBody) {

        _showLoading.value = true
        val service = ApiConfig.getApiService().changePassword(id = user_id, token = token, requestBody = requestBody)
        service.enqueue(object : Callback<StandardRes> {
            override fun onResponse(call: Call<StandardRes>, response: Response<StandardRes>) {
                _showLoading.value = false
                if(response.code() == 502){
                    _returnResponse.postValue(ReturnResponse(status = response.code(), message = response.message()))
                }else {
                    val responseBody = response.body()
                    if(responseBody == null) {
                        _returnResponse.postValue(ReturnResponse(message = response.message()
                            .toString(), status = response.code()))
                    } else {
                        if(!responseBody.success) {
                            _returnResponse.postValue(ReturnResponse(message = responseBody.message,
                                status = 400))
                        } else {
                            responseBody.apply {
                                _returnResponse.postValue(ReturnResponse(message = message,
                                    status = status))
                            }
                        }
                    }
                }
            }

            override fun onFailure(call: Call<StandardRes>, t: Throwable) {
                _showLoading.value = false
                _returnResponse.postValue(ReturnResponse(message = t.message.toString(), status = 500))
            }
        })
    }
}