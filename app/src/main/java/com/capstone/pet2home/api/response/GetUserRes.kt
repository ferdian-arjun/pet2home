package com.capstone.pet2home.api.response

import com.google.gson.annotations.SerializedName

data class GetUserRes(

	@field:SerializedName("result")
	val result: ResultData,

	@field:SerializedName("success")
	val success: Boolean,

	@field:SerializedName("message")
	val message: String,

	@field:SerializedName("status")
	val status: Int
)

data class DataItem(

	@field:SerializedName("password")
	val password: String,

	@field:SerializedName("role")
	val role: String,

	@field:SerializedName("updated_at")
	val updatedAt: String,

	@field:SerializedName("birth_date")
	val birthDate: String,

	@field:SerializedName("birth_place")
	val birthPlace: String,

	@field:SerializedName("created_at")
	val createdAt: String,

	@field:SerializedName("phone_number")
	val phoneNumber: String,

	@field:SerializedName("id_user")
	val idUser: String,

	@field:SerializedName("avatar")
	val avatar: String,

	@field:SerializedName("email")
	val email: String,

	@field:SerializedName("username")
	val username: String,

	@field:SerializedName("status")
	val status: String
)

data class ResultData(

	@field:SerializedName("data")
	val data: List<DataItem?>
)
