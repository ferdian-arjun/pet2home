package com.capstone.pet2home.api

import com.capstone.pet2home.api.response.LoginRes
import com.capstone.pet2home.api.response.RegisterRes
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*

interface ApiService{
    @POST("/login")
    fun login(
        @Body requestBody: RequestBody
    ): Call<LoginRes>

    @POST("/register")
    fun register(
        @Body requestBody: RequestBody
    ): Call<RegisterRes>
}