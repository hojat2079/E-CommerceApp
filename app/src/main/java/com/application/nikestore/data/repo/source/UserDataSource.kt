package com.application.nikestore.data.repo.source

import com.application.nikestore.data.entity.MessageResponse
import com.application.nikestore.data.entity.TokenResponse
import io.reactivex.Single

interface UserDataSource {
    fun login(username: String, password: String): Single<TokenResponse>

    fun register(username: String, password: String): Single<MessageResponse>

    fun loadToken()

    fun saveToken(token: String, refreshToken: String)

    fun saveUsername(username: String)

    fun getUsername(): String

    fun signOut()
}