package com.capstone.pet2home.api.response

import com.google.gson.annotations.SerializedName

data class RegisterRes(
    @field:SerializedName("status")
    val status: Int,

    @field:SerializedName("success")
    val success: Boolean,

    @field:SerializedName("message")
    val message: String
)
