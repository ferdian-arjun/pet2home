package com.capstone.pet2home.ui.settings.editprofile

import androidx.lifecycle.*
import com.capstone.pet2home.api.ApiConfig
import com.capstone.pet2home.api.response.GetUserRes
import com.capstone.pet2home.api.response.StandardRes
import com.capstone.pet2home.helper.ReturnResponse
import com.capstone.pet2home.model.UserModel
import com.capstone.pet2home.preference.UserPreference
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class EditProfileViewModel(private val pref: UserPreference) : ViewModel() {

    private val _showLoading = MutableLiveData<Boolean>()
    val showLoading: LiveData<Boolean> = _showLoading

    private val _returnResponse = MutableLiveData<ReturnResponse>()
    val returnResponse: LiveData<ReturnResponse> = _returnResponse

    fun getUser(): LiveData<UserModel> {
        return pref.getUser().asLiveData()
    }

    private val _userData = MutableLiveData<GetUserRes?>()
    val usersData: LiveData<GetUserRes?> = _userData

    fun getUserApi(userId: String, token: String) {
        _showLoading.value = true
        val client = ApiConfig.getApiService().getUser(userId = userId, token = token)
        client.enqueue(object : Callback<GetUserRes> {
            override fun onResponse(
                call: Call<GetUserRes>,
                response: Response<GetUserRes>
            ) {
                val responseBody = response.body()
                if(responseBody == null){
                    _returnResponse.postValue(ReturnResponse(status = response.code(), message = response.message()))
                }else{
                    if(!responseBody.success){
                        _returnResponse.postValue(ReturnResponse(message = responseBody.message,
                            status = responseBody.status))
                    }else{
                        _userData.postValue(responseBody)
                        _returnResponse.postValue(ReturnResponse(message = responseBody.message,
                            status = responseBody.status))
                        _showLoading.value = false
                    }
                }

            }
            override fun onFailure(call: Call<GetUserRes>, t: Throwable) {
//                _showLoading.value = false
                _returnResponse.postValue(ReturnResponse(message = t.message.toString(), status = 500))
            }
        })
    }

    fun sendData(userId: String, token: String, imageMultipart: MultipartBody.Part? = null, data: Array<RequestBody>) {
        _showLoading.value = true

        val service = ApiConfig.getApiService().editProfile(
            id = userId,
            token = token,
            file = imageMultipart,
            fullName = data[0],
            email = data[1],
            phoneNumber = data[2],
            birthDate = data[3],
            gender = data[4]
        );

        service.enqueue(object : Callback<StandardRes> {
            override fun onResponse(call: Call<StandardRes>, response: Response<StandardRes>, ) {
                if(response.code() == 502){
                    _returnResponse.postValue(ReturnResponse(status = response.code(), message = response.message()))
                }else {
                    val responseBody = response.body()
                    if(responseBody == null) {
                        _returnResponse.postValue(ReturnResponse(message = response.message()
                            .toString(), status = response.code()))
                    } else {
                        if(!responseBody.success) {
                            _returnResponse.postValue(ReturnResponse(message = responseBody.message, status = 400))
                        } else {
                            _returnResponse.postValue(ReturnResponse(message = "Success updates user", status = responseBody.status))
                        }
                    }
                }
                _showLoading.value = false
            }

            override fun onFailure(call: Call<StandardRes>, t: Throwable) {
                _showLoading.value = false
                _returnResponse.postValue(ReturnResponse(message = t.message.toString(), status = 500))
            }
        })
    }

}