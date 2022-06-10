package com.capstone.pet2home.api

import com.capstone.pet2home.api.response.ListPostRes
import com.capstone.pet2home.api.response.LoginRes
import com.capstone.pet2home.api.response.PostRes
import com.capstone.pet2home.api.response.RegisterRes
import com.capstone.pet2home.model.Post
import okhttp3.MultipartBody
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

    @GET("/getallpet")
    fun getAllPost(
    ): Call<ListPostRes>

    @Multipart
    @POST("stories")
    fun uploadPost(
        @Header("Authorization") token: String,
        @Part file: MultipartBody.Part,
        @Part("description") description: RequestBody,
        @Part("lat") lat: RequestBody?,
        @Part("lon") lon: RequestBody?,
        @Part("insta") insta: RequestBody,
        @Part("age") age: RequestBody,
        @Part("title") title: RequestBody,
        @Part("location") location: RequestBody?,
        @Part("whatsapp") whatsapp: RequestBody?,
        @Part("breed") breed: RequestBody?
    ): Call<PostRes>
}