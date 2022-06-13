package com.capstone.pet2home.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class PostModel(
    val idPost: String,
    val idUser: String,
    val description: String,
    val tittle: String,
    val insta: String,
    val whatsapp: String,
    val lon: String,
    val lat: String,
    val pic: String,
    val breed: String,
    val location: String,
    val age: String,
    val updatedAt: String,
    val createdAt: String
): Parcelable
