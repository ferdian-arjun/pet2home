package com.capstone.pet2home.helper

import android.location.Location
import android.util.Patterns
import java.math.RoundingMode
import java.text.DateFormat
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.*

fun CharSequence.isEmailNotValid(): Boolean = !Patterns.EMAIL_ADDRESS.matcher(this).matches()

fun String.withDateFormat(): String {
    val format = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.ENGLISH)
    val date = format.parse(this) as Date
    return DateFormat.getDateInstance(DateFormat.FULL).format(date)
}

fun String.setData(): String {
    return SimpleDateFormat("dd MMMM yyyy", Locale.ENGLISH).format(this)
}

fun Float.roundOffDecimal(): Float {
    val df = DecimalFormat("#.#")
    df.roundingMode = RoundingMode.FLOOR
    return df.format(this).toFloat()
}

fun Float.convertMeterToKilometer(): Float {
    return (this * 0.001).toFloat().roundOffDecimal()
}

fun checkDistance(myLat: Double?, myLon: Double?, lat: String, lon: String): Float {
    val results = FloatArray(1)
    val getLatPost = if(lat.isEmpty()) myLat else lat.trim().toDouble()
    val getLonPost = if(lon.isEmpty()) myLon else lon.trim().toDouble()
    Location.distanceBetween(myLat!!,myLon!!,getLatPost!!,getLonPost!!, results)
    return results[0]
}

data class ReturnResponse(
    var message: String,
    var status: Int,
)