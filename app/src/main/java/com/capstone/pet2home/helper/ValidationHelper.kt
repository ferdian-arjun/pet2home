package com.capstone.pet2home.helper

import android.util.Patterns

class ValidationHelper {

    fun validEmail(value: String): String? {
        if(!Patterns.EMAIL_ADDRESS.matcher(value).matches())
        {
            return "Invalid Email Address"
        }
        return null
    }

    fun validPassword(value: String): String? {
        if(value.length < 6)
        {
            return "Minumum 6 Character Password"
        }
        if(!value.matches(".*[A-Z].*".toRegex()))
        {
            return "Must Contain 1 Upper-case Character"
        }
        if(!value.matches(".*[a-z].*".toRegex()))
        {
            return "Must Contain 1 Lower-case Character"
        }
        return null
    }

    fun validFullName(value: String): String? {
        if(!value.matches("^[a-zA-Z]{4,}(?: [a-zA-Z]+){0,2}\$".toRegex()))
        {
            return "This Not Name Person"
        }
        return null
    }

    fun validPhoneNumber(value: String): String? {
        if(!value.matches("^(^\\+62|62|^08)(\\d{3,4}-?){2}\\d{3,4}\$".toRegex()))
        {
            return "Invalid Phone Number"
        }
        return null
    }
}