package com.capstone.pet2home.api

import com.capstone.pet2home.api.response.GetUserRes
import com.capstone.pet2home.api.response.LoginRes
import com.capstone.pet2home.api.response.RegisterRes
import com.capstone.pet2home.api.response.StandardRes
import com.capstone.pet2home.ui.settings.editprofile.EditProfileActivity.Companion.BIRTH_DATE
import com.capstone.pet2home.ui.settings.editprofile.EditProfileActivity.Companion.FULL_NAME
import com.capstone.pet2home.ui.settings.editprofile.EditProfileActivity.Companion.EMAIL
import com.capstone.pet2home.ui.settings.editprofile.EditProfileActivity.Companion.GENDER
import com.capstone.pet2home.ui.settings.editprofile.EditProfileActivity.Companion.PHONE_NUMBER
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

    @GET("/profile/{id}")
    fun getUser(
        @Path("id") userId : String,
        @Header("x-access-token") token: String
    ): Call<GetUserRes>

    @Multipart
    @PUT("/profile/update/{id}")
    fun editProfile(
        @Path("id") id : String,
        @Header("x-access-token") token: String,
        @Part file: MultipartBody.Part? = null,
        @Part(FULL_NAME) fullName: RequestBody,
        @Part(EMAIL) email: RequestBody,
        @Part(BIRTH_DATE) birthDate: RequestBody? = null,
        @Part(GENDER) gender: RequestBody? = null,
        @Part(PHONE_NUMBER) phoneNumber: RequestBody? = null,
    ): Call<StandardRes>

    @PUT("/changePassword/{id}")
    fun changePassword(
        @Path("id") id : String,
        @Header("x-access-token") token: String,
        @Body requestBody: RequestBody
    ): Call<StandardRes>
}