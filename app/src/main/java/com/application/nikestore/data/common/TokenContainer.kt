package com.application.nikestore.data.common

import timber.log.Timber

object TokenContainer {
    var token: String? = null
        private set
    var refreshToken: String? = null
        private set

    fun update(token: String?, refreshToken: String?) {
        Timber.i("Access Token-> ${token?.substring(0, 10)}, Refresh Token-> $refreshToken")
        TokenContainer.token = token
        TokenContainer.refreshToken = refreshToken
    }
}