package com.application.nikestore.feature.checkout

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.application.nikestore.common.NikeSingleObserver
import com.application.nikestore.common.NikeViewModel
import com.application.nikestore.data.entity.Checkout
import com.application.nikestore.data.repo.OrderRepository
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class CheckOutViewModel(private val orderId: Int, private val orderRepository: OrderRepository) :
    NikeViewModel() {
    private val _checkOutLiveData = MutableLiveData<Checkout>()
    val checkOutLiveData: LiveData<Checkout> get() = _checkOutLiveData

    init {
        orderRepository.checkout(orderId).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : NikeSingleObserver<Checkout>(compositeDisposable) {
                override fun onSuccess(t: Checkout) {
                    _checkOutLiveData.value = t
                }
            })
    }
}