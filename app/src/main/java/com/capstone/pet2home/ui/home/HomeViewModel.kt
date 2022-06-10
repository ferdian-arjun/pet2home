package com.capstone.pet2home.ui.home

import android.content.ContentValues.TAG
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.capstone.pet2home.api.ApiConfig
import com.capstone.pet2home.api.response.DataItem
import com.capstone.pet2home.api.response.ListPostRes
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeViewModel : ViewModel() {
    private val _listPost = MutableLiveData<List<DataItem>>()
    val listPost: LiveData<List<DataItem>> = _listPost

    init{
        getAllPost()
    }

    private fun getAllPost(){
        val service =ApiConfig.getApiService().getAllPost()
        service.enqueue(object : Callback<ListPostRes>{
            override fun onResponse(call: Call<ListPostRes>, response: Response<ListPostRes>) {
                if(response.isSuccessful){
                    _listPost.value = response.body()?.result?.data
                }else{
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<ListPostRes>, t: Throwable) {
                Log.e(TAG, "onFailure: ${t.message.toString()}")
            }

        })
    }


   /* private val _text = MutableLiveData<String>().apply {
        value = "This is home Fragment"
    }
    val text: LiveData<String> = _text*/
}