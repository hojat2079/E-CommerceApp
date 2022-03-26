package com.application.nikestore.data.repo.source

import com.application.nikestore.data.entity.MessageResponse
import com.application.nikestore.data.entity.TokenResponse
import com.application.nikestore.service.http.ApiService
import com.google.gson.JsonObject
import io.reactivex.Single

const val CLIENT_ID = 2
const val CLIENT_SECRET = "kyj1c9sVcksqGU4scMX7nLDalkjp2WoqQEf8PKAC"

class UserRemoteDataSource(private val apiService: ApiService) : UserDataSource {
    override fun login(username: String, password: String): Single<TokenResponse> {
        return apiService.login(JsonObject().apply {
            addProperty("username", username)
            addProperty("password", password)
            addProperty("grant_type", "password")
            addProperty("client_id", CLIENT_ID)
            addProperty("client_secret", CLIENT_SECRET)
        })
    }

    override fun register(username: String, password: String): Single<MessageResponse> {
        return apiService.register(JsonObject().apply {
            addProperty("email", username)
            addProperty("password", password)

        })
    }

    override fun loadToken() {
        TODO("Not yet implemented")
    }

    override fun saveToken(token: String, refreshToken: String) {
        TODO("Not yet implemented")
    }

    override fun saveUsername(username: String) {
        TODO("Not yet implemented")
    }

    override fun getUsername(): String {
        TODO("Not yet implemented")
    }

    override fun signOut() {
        TODO("Not yet implemented")
    }
}