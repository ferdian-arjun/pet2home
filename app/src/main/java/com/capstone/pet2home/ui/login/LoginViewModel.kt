package com.capstone.pet2home.ui.login

import androidx.lifecycle.*
import com.capstone.pet2home.api.ApiConfig
import com.capstone.pet2home.api.response.LoginRes
import com.capstone.pet2home.helper.ReturnResponse
import com.capstone.pet2home.model.UserModel
import com.capstone.pet2home.preference.UserPreference
import kotlinx.coroutines.launch
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginViewModel(private val pref: UserPreference) : ViewModel() {


    private val _returnResponse = MutableLiveData<ReturnResponse>()
    val returnResponse: LiveData<ReturnResponse> = _returnResponse

    fun getUser(): LiveData<UserModel> {
        return pref.getUser().asLiveData()
    }

    fun login(user: UserModel) {
        viewModelScope.launch {
            pref.login(user)
        }
    }




    fun loginUser(requestBody: RequestBody) {
        val service = ApiConfig.getApiService().login(requestBody)
        service.enqueue(object : Callback<LoginRes> {
            override fun onResponse(call: Call<LoginRes>, response: Response<LoginRes>) {
                val responseBody = response.body()
                if(responseBody != null){
                    if(responseBody.success){
                        responseBody.apply {
                            login(UserModel(
                                userId = result.userId,
                                email = result.email,
                                fullName = result.fullname,
                                token = result.token,
                                isLogin = true
                            ))

                            _returnResponse.postValue(ReturnResponse(message= responseBody.message,status = responseBody.status))
                        }
                    }
                }else{
                    _returnResponse.postValue(ReturnResponse(message= response.message().toString(),status = response.code()))
                }
            }

            override fun onFailure(call: Call<LoginRes>, t: Throwable) {
                _returnResponse.postValue(ReturnResponse(message= t.message.toString(),status = 500))
            }
        })
    }
}


