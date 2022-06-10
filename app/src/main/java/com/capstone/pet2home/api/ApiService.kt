package com.capstone.pet2home.api

import com.capstone.pet2home.api.response.*
import com.capstone.pet2home.ui.postadd.PostAddActivity.Companion.AGE
import com.capstone.pet2home.ui.postadd.PostAddActivity.Companion.BREED
import com.capstone.pet2home.ui.postadd.PostAddActivity.Companion.DESCRIPTION
import com.capstone.pet2home.ui.postadd.PostAddActivity.Companion.ID_USER
import com.capstone.pet2home.ui.postadd.PostAddActivity.Companion.INSTAGRAM
import com.capstone.pet2home.ui.postadd.PostAddActivity.Companion.LOCATION
import com.capstone.pet2home.ui.postadd.PostAddActivity.Companion.TITLE
import com.capstone.pet2home.ui.postadd.PostAddActivity.Companion.WHATSAPP
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

    @GET("/profile/pet/{id}")
    fun getPetByUser(
        @Path("id") userId : String,
        @Header("x-access-token") token: String
    ): Call<GetPetByUserRes>

    @Multipart
    @POST("/postPet")
    fun createPost(
        @Header("x-access-token") token: String,
        @Part file: MultipartBody.Part,
        @Part(DESCRIPTION) description: RequestBody,
        @Part(INSTAGRAM) instagram: RequestBody,
        @Part(AGE) age: RequestBody,
        @Part(ID_USER) idUser: RequestBody,
        @Part(TITLE) title: RequestBody,
        @Part(BREED) breed: RequestBody,
        @Part(LOCATION) location: RequestBody,
        @Part(WHATSAPP) whatsApp: RequestBody,
    ): Call<StandardRes>

    @DELETE("/post/delete/{id}")
    fun deletePost(
        @Path("id") postId : String,
        @Header("x-access-token") token: String
    ): Call<StandardRes>
}