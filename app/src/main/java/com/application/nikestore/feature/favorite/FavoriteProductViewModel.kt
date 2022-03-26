package com.application.nikestore.feature.favorite

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.application.nikestore.common.NikeCompletableObserver
import com.application.nikestore.common.NikeSingleObserver
import com.application.nikestore.common.NikeViewModel
import com.application.nikestore.data.entity.EmptyState
import com.application.nikestore.data.entity.Product
import com.application.nikestore.data.repo.ProductRepository
import io.reactivex.schedulers.Schedulers
import timber.log.Timber

class FavoriteProductViewModel(
    private val productRepository: ProductRepository
) : NikeViewModel() {
    private val _favoriteProductLiveData = MutableLiveData<List<Product>>()
    val favoriteProductLiveData: LiveData<List<Product>> get() = _favoriteProductLiveData

    init {
        productRepository.getFavoriteProducts().subscribeOn(Schedulers.io()).subscribe(object :
            NikeSingleObserver<List<Product>>(compositeDisposable) {
            override fun onSuccess(t: List<Product>) {
                _favoriteProductLiveData.postValue(t)
            }
        })
    }

    fun removeFromFavorite(product: Product, size: Int) {
        productRepository.deleteFromFavorites(product).subscribeOn(Schedulers.io())
            .subscribe(object : NikeCompletableObserver(compositeDisposable) {
                override fun onComplete() {
                    if (size == 0)
                        _favoriteProductLiveData.postValue(arrayListOf())
                }
            })
    }
}