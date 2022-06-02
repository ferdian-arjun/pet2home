package com.capstone.pet2home.helper

import android.util.Patterns
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

fun CharSequence.isEmailNotValid(): Boolean = !Patterns.EMAIL_ADDRESS.matcher(this).matches()

fun String.withDateFormat(): String {
    val format = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.US)
    val date = format.parse(this) as Date
    return DateFormat.getDateInstance(DateFormat.FULL).format(date)
}

data class ReturnResponse(
    var message: String,
    var status: Int
)