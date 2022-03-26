package com.application.nikestore.data.repo

import io.reactivex.Completable

interface UserRepository {

    fun login(username: String, password: String): Completable

    fun register(username: String, password: String): Completable

    fun loadToken()

    fun signOut()

    fun getUsername():String
}