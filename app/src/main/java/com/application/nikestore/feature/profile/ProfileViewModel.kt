package com.application.nikestore.feature.profile

import com.application.nikestore.common.NikeViewModel
import com.application.nikestore.data.common.TokenContainer
import com.application.nikestore.data.repo.UserRepository
import org.greenrobot.eventbus.EventBus

class ProfileViewModel(private val repository: UserRepository) : NikeViewModel() {
    val isSigned: Boolean
        get() = !TokenContainer.token.isNullOrEmpty()
    val username: String
        get() = repository.getUsername()

    fun signOut() {
        repository.signOut()
        //remove badge
        EventBus.getDefault().postSticky("")

    }

}