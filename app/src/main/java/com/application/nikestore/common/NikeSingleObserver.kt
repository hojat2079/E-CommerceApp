package com.application.nikestore.common

import io.reactivex.SingleObserver
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import org.greenrobot.eventbus.EventBus
import timber.log.Timber

abstract class NikeSingleObserver<T>(private val compositeDisposable: CompositeDisposable) : SingleObserver<T> {
    override fun onError(e: Throwable) {
        Timber.e("$e")
        EventBus.getDefault().post(NikeExceptionMapper.map(e))
    }

    override fun onSubscribe(d: Disposable) {
        compositeDisposable.add(d)
    }
}