package com.application.nikestore.feature.auth

import com.application.nikestore.common.NikeViewModel
import com.application.nikestore.data.repo.UserRepository
import io.reactivex.Completable

class AuthViewModel(private val userRepository: UserRepository) : NikeViewModel() {

    fun login(email: String, password: String): Completable {
        progressBarLiveData.value = true
        return userRepository.login(username = email, password).doFinally {
            progressBarLiveData.postValue(false)
        }
    }
    fun register(email:String,password:String):Completable{
        progressBarLiveData.value = true
        return userRepository.register(username = email, password).doFinally {
            progressBarLiveData.postValue(false)
        }
    }
}