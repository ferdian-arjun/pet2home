package com.capstone.pet2home.ui.postedit

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.capstone.pet2home.api.ApiConfig
import com.capstone.pet2home.api.response.DataItemPet
import com.capstone.pet2home.api.response.GetPetByUserRes
import com.capstone.pet2home.api.response.StandardRes
import com.capstone.pet2home.helper.ReturnResponse
import com.capstone.pet2home.model.UserModel
import com.capstone.pet2home.preference.UserPreference
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PostEditViewModel(private val pref: UserPreference) : ViewModel() {

    private val _showLoading = MutableLiveData<Boolean>()
    val showLoading: LiveData<Boolean> = _showLoading

    private val _returnResponse = MutableLiveData<ReturnResponse>()
    val returnResponse: LiveData<ReturnResponse> = _returnResponse

    fun getUser(): LiveData<UserModel> {
        return pref.getUser().asLiveData()
    }

    private val _dataPost = MutableLiveData<DataItemPet?>()
    val dataPost: LiveData<DataItemPet?> = _dataPost

    fun getPetApi(petId: String, token: String) {
        _showLoading.value = true
        val client = ApiConfig.getApiService().getPet(petId = petId, token = token)
        client.enqueue(object : Callback<GetPetByUserRes> {
            override fun onResponse(
                call: Call<GetPetByUserRes>,
                response: Response<GetPetByUserRes>
            ) {
                val responseBody = response.body()
                if(responseBody == null){
                    _returnResponse.postValue(ReturnResponse(status = response.code(), message = response.message()))
                }else{
                    if(responseBody.success){
                        _returnResponse.postValue(ReturnResponse(message = responseBody.message,
                            status = responseBody.status))
                    }else{
                        _dataPost.postValue(responseBody.result.data[0])
                        _returnResponse.postValue(ReturnResponse(message = responseBody.message,
                            status = responseBody.status))
                    }
                    _showLoading.value = false
                }

            }
            override fun onFailure(call: Call<GetPetByUserRes>, t: Throwable) {
                _showLoading.value = false
                _returnResponse.postValue(ReturnResponse(message = t.message.toString(), status = 500))
            }
        })
    }


    fun updatePost(idPost: String, token: String, image: MultipartBody.Part? = null, data: Array<RequestBody>) {
        _showLoading.value = true
        val service = ApiConfig.getApiService().updatePost(
            id = idPost,
            token = token,
            file = image,
            description = data[0],
            instagram = data[1],
            age = data[2],
            idUser = data[3],
            title = data[4],
            breed = data[5],
            location = data[6],
            whatsApp = data[7]
        )
        service.enqueue(object : Callback<StandardRes> {
            override fun onResponse(call: Call<StandardRes>, response: Response<StandardRes>) {
                if(response.code() == 400){
                    _returnResponse.postValue(ReturnResponse(status = response.code(), message = response.message()))
                }else{
                    val responseBody = response.body()
                    if(responseBody != null){
                        _returnResponse.postValue(ReturnResponse(status = responseBody.status, message = responseBody.message))
                    }else{
                        _returnResponse.postValue(ReturnResponse(status = response.code(), message = response.body()?.error.toString()))
                    }
                }

                _showLoading.value = false
            }

            override fun onFailure(call: Call<StandardRes>, t: Throwable) {
                _returnResponse.postValue(ReturnResponse(status = 500, message = t.message.toString()))
                _showLoading.value = false
            }
        })
    }
}