package com.application.nikestore.data.repo.source

import android.content.SharedPreferences
import com.application.nikestore.data.entity.MessageResponse
import com.application.nikestore.data.common.TokenContainer
import com.application.nikestore.data.entity.TokenResponse
import io.reactivex.Single

class UserLocalDataSource(private val sharedPreferences: SharedPreferences) : UserDataSource {
    override fun login(username: String, password: String): Single<TokenResponse> {
        TODO("Not yet implemented")
    }

    override fun register(username: String, password: String): Single<MessageResponse> {
        TODO("Not yet implemented")
    }

    override fun loadToken() {
        TokenContainer.update(
            sharedPreferences.getString("access_token", null),
            sharedPreferences.getString("refresh_token", null)
        )
    }

    override fun saveToken(token: String, refreshToken: String) {
        sharedPreferences.edit().apply {
            putString("access_token", token)
            putString("refresh_token", refreshToken)
        }.apply()
    }

    override fun saveUsername(username: String) {
        sharedPreferences.edit().apply {
            putString("username", username)
        }.apply()
    }

    override fun getUsername(): String =
        sharedPreferences.getString("username", "") ?: ""

    override fun signOut() {
        sharedPreferences.edit().clear().apply()
    }
}