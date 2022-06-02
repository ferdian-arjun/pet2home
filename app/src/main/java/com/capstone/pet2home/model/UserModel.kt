package com.capstone.pet2home.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class UserModel(
    val userId: String,
    val email: String,
    val fullName: String,
    val token: String,
    val isLogin: Boolean
): Parcelable
