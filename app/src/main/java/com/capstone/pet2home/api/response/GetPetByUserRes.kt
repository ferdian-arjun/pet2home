package com.capstone.pet2home.api.response

import com.google.gson.annotations.SerializedName

data class GetPetByUserRes(

	@field:SerializedName("result")
	val result: ResultDatPet,

	@field:SerializedName("success")
	val success: Boolean,

	@field:SerializedName("message")
	val message: String,

	@field:SerializedName("status")
	val status: Int
)

data class DataItemPet(

	@field:SerializedName("insta")
	val insta: String,

	@field:SerializedName("whatsapp")
	val whatsapp: String,

	@field:SerializedName("role")
	val role: String,

	@field:SerializedName("gender")
	val gender: String,

	@field:SerializedName("id_post")
	val idPost: String,

	@field:SerializedName("birth_date")
	val birthDate: String,

	@field:SerializedName("description")
	val description: String,

	@field:SerializedName("created_at")
	val createdAt: String,

	@field:SerializedName("birth_place")
	val birthPlace: String,

	@field:SerializedName("lon")
	val lon: String,

	@field:SerializedName("id_user")
	val idUser: String,

	@field:SerializedName("pic")
	val pic: String,

	@field:SerializedName("avatar")
	val avatar: String,

	@field:SerializedName("title")
	val title: String,

	@field:SerializedName("breed")
	val breed: String,

	@field:SerializedName("password")
	val password: String,

	@field:SerializedName("updated_at")
	val updatedAt: String,

	@field:SerializedName("location")
	val location: String,

	@field:SerializedName("phone_number")
	val phoneNumber: String,

	@field:SerializedName("age")
	val age: String,

	@field:SerializedName("lat")
	val lat: String,

	@field:SerializedName("email")
	val email: String,

	@field:SerializedName("username")
	val username: String,

	@field:SerializedName("status")
	val status: String
)

data class ResultDatPet(

	@field:SerializedName("data")
	val data: List<DataItemPet>
)
