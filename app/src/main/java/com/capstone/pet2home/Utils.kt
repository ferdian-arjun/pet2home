package com.capstone.pet2home

import android.util.Patterns

fun emailFormat(email: CharSequence): Boolean {
    return Patterns.EMAIL_ADDRESS.matcher(email).matches()
}