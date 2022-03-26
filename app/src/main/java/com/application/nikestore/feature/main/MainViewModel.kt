package com.application.nikestore.feature.main

import com.application.nikestore.common.NikeSingleObserver
import com.application.nikestore.common.NikeViewModel
import com.application.nikestore.data.entity.CartItemCount
import com.application.nikestore.data.common.TokenContainer
import com.application.nikestore.data.repo.CartRepository
import io.reactivex.schedulers.Schedulers
import org.greenrobot.eventbus.EventBus

class MainViewModel(private val cartRepository: CartRepository) : NikeViewModel() {
    fun getCartItemCount() {
        if (!TokenContainer.token.isNullOrEmpty()) {
            cartRepository.getCartItemsCount().subscribeOn(Schedulers.io()).subscribe(object :
                NikeSingleObserver<CartItemCount>(compositeDisposable) {
                override fun onSuccess(t: CartItemCount) {
                    EventBus.getDefault().postSticky(t)
                }
            })
        }
    }
}