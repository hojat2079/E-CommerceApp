package com.application.nikestore.feature.product

import android.os.Bundle
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.application.nikestore.common.EXTRA_KEYS_DATA
import com.application.nikestore.common.NikeSingleObserver
import com.application.nikestore.common.NikeViewModel
import com.application.nikestore.common.asyncNetworkRequest
import com.application.nikestore.data.entity.Comment
import com.application.nikestore.data.entity.Product
import com.application.nikestore.data.repo.CartRepository
import com.application.nikestore.data.repo.CommentRepository
import io.reactivex.Completable

class ProductDetailViewModel(
    private val bundle: Bundle,
    private val commentRepository: CommentRepository, private val cartRepository: CartRepository
) : NikeViewModel() {
    private val _productLiveData = MutableLiveData<Product>()
    val productLiveData: LiveData<Product> get() = _productLiveData
    private val _commentLiveData = MutableLiveData<List<Comment>>()
    val commentLiveData: LiveData<List<Comment>> get() = _commentLiveData

    init {
        progressBarLiveData.value = true
        _productLiveData.value = bundle.getParcelable(EXTRA_KEYS_DATA)
        commentRepository.getAll(productLiveData.value!!.id).asyncNetworkRequest()
            .doFinally { progressBarLiveData.value = false }
            .subscribe(object : NikeSingleObserver<List<Comment>>(compositeDisposable) {
                override fun onSuccess(t: List<Comment>) {
                    _commentLiveData.value = t
                }

            })
    }

    fun addToCart(): Completable {
        return cartRepository.addToCart(productLiveData.value!!.id).ignoreElement()
    }
}