package com.capstone.pet2home.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class Post (
    var location: String? = null,
    val insta: String? = null,
    val whatsapp: String? = null,
    val description: String? = null,
    val createdAt: String,
    val lon: Double? = null,
    val pic: String? = null,
    val breed: String? = null,
    val updatedAt: String? = null,
    val tittle: String? = null,
    val age: String? = null,
    val lat: Double? = null
): Parcelable