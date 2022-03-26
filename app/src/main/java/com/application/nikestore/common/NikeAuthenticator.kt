package com.application.nikestore.common

import com.application.nikestore.data.common.TokenContainer
import com.application.nikestore.data.entity.TokenResponse
import com.application.nikestore.data.repo.source.CLIENT_ID
import com.application.nikestore.data.repo.source.CLIENT_SECRET
import com.application.nikestore.data.repo.source.UserDataSource
import com.application.nikestore.service.http.ApiService
import com.google.gson.JsonObject
import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route
import org.koin.core.component.KoinApiExtension
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import timber.log.Timber

@KoinApiExtension
class NikeAuthenticator : Authenticator, KoinComponent {
    private val apiService: ApiService by inject()
    private val userLocalDataSource: UserDataSource by inject()
    override fun authenticate(route: Route?, response: Response): Request? {
        if (TokenContainer.token != null && TokenContainer.refreshToken != null && !response.request.url.pathSegments.last().equals ("token",false)) {
            try {
                val token = refreshToken()
                if (token.isEmpty())
                    return null
                return response.request.newBuilder().header("Authorization", "Bearer $token")
                    .build()
            } catch (ex: Exception) {
                Timber.e(ex)
            }
        }
        return null
    }

    private fun refreshToken(): String {
        val response: retrofit2.Response<TokenResponse> =
            apiService.refreshToken(JsonObject().apply {
                addProperty("grant_type", "refresh_token")
                addProperty("refresh_token", TokenContainer.refreshToken)
                addProperty("client_id", CLIENT_ID)
                addProperty("client_secret", CLIENT_SECRET)
            }).execute()
        response.body()?.let {
            TokenContainer.update(it.access_token, it.refresh_token)
            userLocalDataSource.saveToken(it.access_token, it.refresh_token)
            return it.access_token
        }
        return ""
    }
}