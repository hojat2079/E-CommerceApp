package com.application.nikestore.feature.list

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.application.nikestore.R
import com.application.nikestore.common.NikeCompletableObserver
import com.application.nikestore.common.NikeSingleObserver
import com.application.nikestore.common.NikeViewModel
import com.application.nikestore.common.asyncNetworkRequest
import com.application.nikestore.data.entity.Product
import com.application.nikestore.data.repo.ProductRepository
import io.reactivex.schedulers.Schedulers

class ProductListViewModel(
    var sort: Int,
    private val productRepository: ProductRepository
) :
    NikeViewModel() {
    private val _productsLiveData = MutableLiveData<List<Product>>()
    val productsLiveData: LiveData<List<Product>> get() = _productsLiveData
    private val _selectedSortLiveData = MutableLiveData<Int>()
    val selectedSortLiveData: LiveData<Int> get() = _selectedSortLiveData
    private val sortList = arrayOf(
        R.string.latest,
        R.string.popular,
        R.string.sortPriceLowToHigh,
        R.string.sortPriceLHighToLow
    )


    init {
        getProducts()
        _selectedSortLiveData.value = sortList[sort]
    }

    private fun getProducts() {
        progressBarLiveData.value = true
        productRepository.getProducts(sort).asyncNetworkRequest()
            .doFinally { progressBarLiveData.value = false }
            .subscribe(object : NikeSingleObserver<List<Product>>(compositeDisposable) {
                override fun onSuccess(t: List<Product>) {
                    _productsLiveData.value = t
                }

            })

    }

    fun changeSortItems(sort: Int) {
        this.sort = sort
        getProducts()
        _selectedSortLiveData.value = sortList[sort]

    }

    fun setFavoriteInProduct(product: Product) {
        if (product.isFavorite) {
            productRepository.deleteFromFavorites(product).subscribeOn(Schedulers.io())
                .subscribe(object : NikeCompletableObserver(compositeDisposable) {
                    override fun onComplete() {
                        product.isFavorite = false
                    }
                })
        } else {
            productRepository.addToFavorite(product).subscribeOn(Schedulers.io())
                .subscribe(object : NikeCompletableObserver(compositeDisposable) {
                    override fun onComplete() {
                        product.isFavorite = true
                    }
                })
        }
    }
}