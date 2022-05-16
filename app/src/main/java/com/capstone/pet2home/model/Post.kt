package com.capstone.pet2home.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Post (
    var title: String,
    var location: String,
    var image: Int
): Parcelable