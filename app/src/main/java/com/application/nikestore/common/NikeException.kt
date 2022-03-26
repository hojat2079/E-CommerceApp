package com.application.nikestore.common

import androidx.annotation.StringRes

class NikeException(val type: Type, @StringRes val userMessage: Int=0, val serverMessage: String?=null) :
    Throwable() {
    enum class Type {
        SIMPLE, DIALOG, AUT
    }
}