package com.capstone.pet2home.api.response

import com.google.gson.annotations.SerializedName

data class ListPostRes(

	@field:SerializedName("result")
	val result: ListData,

	@field:SerializedName("succes")
	val succes: Boolean,

	@field:SerializedName("message")
	val message: String,

	@field:SerializedName("status")
	val status: Int
)

data class ListData(

	@field:SerializedName("data")
	val data: List<DataItem>
)

data class DataItem(

	@field:SerializedName("insta")
	val insta: String,

	@field:SerializedName("whatsapp")
	val whatsapp: String,

	@field:SerializedName("id_post")
	val idPost: String,

	@field:SerializedName("description")
	val description: String,

	@field:SerializedName("created_at")
	val createdAt: String,

	@field:SerializedName("lon")
	val lon: Double,

	@field:SerializedName("id_user")
	val idUser: String,

	@field:SerializedName("pic")
	val pic: String,

	@field:SerializedName("breed")
	val breed: String,

	@field:SerializedName("updated_at")
	val updatedAt: String,

	@field:SerializedName("location")
	val location: String,

	@field:SerializedName("tittle")
	val tittle: String,

	@field:SerializedName("age")
	val age: String,

	@field:SerializedName("lat")
	val lat: Double
)
