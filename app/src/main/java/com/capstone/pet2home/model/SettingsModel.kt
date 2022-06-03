package com.capstone.pet2home.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class SettingsModel(
    var language: String? = null,
    var isOnboardPage: Boolean? = null
): Parcelable
