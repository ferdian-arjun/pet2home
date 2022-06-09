package com.capstone.pet2home.ui.profile

import androidx.lifecycle.*
import com.capstone.pet2home.api.ApiConfig
import com.capstone.pet2home.api.response.DataItemPet
import com.capstone.pet2home.api.response.GetPetByUserRes
import com.capstone.pet2home.api.response.GetUserRes
import com.capstone.pet2home.helper.ReturnResponse
import com.capstone.pet2home.model.UserModel
import com.capstone.pet2home.preference.UserPreference
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProfileViewModel(private val pref: UserPreference) : ViewModel() {

    fun getUser(): LiveData<UserModel> {
        return pref.getUser().asLiveData()
    }

    private val _returnResponse = MutableLiveData<ReturnResponse>()
    val returnResponse: LiveData<ReturnResponse> = _returnResponse

    private val _showLoading = MutableLiveData<Boolean>()
    val showLoading: LiveData<Boolean> = _showLoading

    private val _userData = MutableLiveData<GetUserRes?>()
    val usersData: LiveData<GetUserRes?> = _userData

    private val _countPostUser = MutableLiveData<Int>()
    val countPostUser: LiveData<Int> = _countPostUser

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
                    }
                }
                _showLoading.value = false
            }
            override fun onFailure(call: Call<GetUserRes>, t: Throwable) {
                _showLoading.value = false
                _returnResponse.postValue(ReturnResponse(message = t.message.toString(), status = 500))
            }
        })
    }

    private val _userPetData = MutableLiveData<List<DataItemPet>?>()
    val userPetData: LiveData<List<DataItemPet>?> = _userPetData

    fun getUserPetApi(userId: String, token: String) {
        _showLoading.value = true
        val client = ApiConfig.getApiService().getPetByUser(userId = userId, token = token)
        client.enqueue(object : Callback<GetPetByUserRes>{
            override fun onResponse(call: Call<GetPetByUserRes>, response: Response<GetPetByUserRes>) {
                val responseBody = response.body()
                if(responseBody == null){
                    _returnResponse.postValue(ReturnResponse(status = response.code(), message = response.message()))
                }else{
                    if(!responseBody.success){
                        _returnResponse.postValue(ReturnResponse(message = responseBody.message,
                            status = responseBody.status))
                    }else{
                        _countPostUser.value = responseBody.result.data.count()
                        _userPetData.postValue(responseBody.result.data)

                        _returnResponse.postValue(ReturnResponse(message = responseBody.message,
                            status = responseBody.status))
                    }
                }
                _showLoading.value = false
            }

            override fun onFailure(call: Call<GetPetByUserRes>, t: Throwable) {
                _showLoading.value = false
                _returnResponse.postValue(ReturnResponse(message = t.message.toString(), status = 500))
            }
        })
    }
}