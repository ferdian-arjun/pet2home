package com.capstone.pet2home.api.response

import com.google.gson.annotations.SerializedName

data class LoginRes(

	@field:SerializedName("result")
	val result: Result,

	@field:SerializedName("error")
	val success: Boolean,

	@field:SerializedName("message")
	val message: String,

	@field:SerializedName("status")
	val status: Int
)

data class Result(

	@field:SerializedName("username")
	val username: String,

	@field:SerializedName("userId")
	val userId: String,

	@field:SerializedName("email")
	val email: String,

	@field:SerializedName("token")
	val token: String
)
