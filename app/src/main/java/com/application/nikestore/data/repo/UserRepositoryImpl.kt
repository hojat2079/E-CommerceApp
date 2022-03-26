package com.application.nikestore.data.repo

import com.application.nikestore.data.common.TokenContainer
import com.application.nikestore.data.entity.TokenResponse
import com.application.nikestore.data.repo.source.UserDataSource
import io.reactivex.Completable

class UserRepositoryImpl(
    private val userRemoteDataSource: UserDataSource,
    private val userLocalDataSource: UserDataSource
) : UserRepository {
    override fun login(username: String, password: String): Completable {
        return userRemoteDataSource.login(username, password).doOnSuccess {
            onSuccessfulLogin(username, it)
        }.ignoreElement()
    }


    override fun register(username: String, password: String): Completable {
        return userRemoteDataSource.register(username, password).flatMap {
            userRemoteDataSource.login(username, password).doOnSuccess {
                onSuccessfulLogin(username, it)
            }
        }.ignoreElement()
    }

    override fun loadToken() {
        userLocalDataSource.loadToken()
    }

    override fun signOut() {
        userLocalDataSource.signOut()
        TokenContainer.update(null, null)
    }

    override fun getUsername(): String = userLocalDataSource.getUsername()

    private fun onSuccessfulLogin(username: String, tokenResponse: TokenResponse) {
        TokenContainer.update(tokenResponse.access_token, tokenResponse.refresh_token)
        userLocalDataSource.saveToken(tokenResponse.access_token, tokenResponse.refresh_token)
        userLocalDataSource.saveUsername(username)
    }
}