package com.application.nikestore.feature.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.application.nikestore.common.NikeCompletableObserver
import com.application.nikestore.common.NikeSingleObserver
import com.application.nikestore.common.NikeViewModel
import com.application.nikestore.common.asyncNetworkRequest
import com.application.nikestore.data.entity.Banner
import com.application.nikestore.data.entity.Product
import com.application.nikestore.data.entity.SORT_LATEST
import com.application.nikestore.data.entity.SORT_POPULAR
import com.application.nikestore.data.repo.BannerRepository
import com.application.nikestore.data.repo.ProductRepository
import io.reactivex.functions.BiFunction
import io.reactivex.schedulers.Schedulers

class HomeViewModel(
    private val productRepository: ProductRepository,
    bannerRepository: BannerRepository
) :
    NikeViewModel() {
    private val _latestProductLivaData = MutableLiveData<List<Product>>()
    private val _popularProductLivaData = MutableLiveData<List<Product>>()
    private val _bannerLivaData = MutableLiveData<List<Banner>>()
    val latestProductLivaData: LiveData<List<Product>> get() = _latestProductLivaData
    val popularProductLivaData: LiveData<List<Product>> get() = _popularProductLivaData
    val bannerLivaData: LiveData<List<Banner>> get() = _bannerLivaData

    init {
        progressBarLiveData.value = true
        getProducts()

        //slider
        bannerRepository.getBanner().asyncNetworkRequest()
            .subscribe(object : NikeSingleObserver<List<Banner>>(compositeDisposable) {
                override fun onSuccess(banner: List<Banner>) {
                    _bannerLivaData.value = banner
                }
            })


    }

    fun setFavoriteInProduct(product: Product) {
        if (product.isFavorite) {
            productRepository.deleteFromFavorites(product).subscribeOn(Schedulers.io())
                .subscribe(object : NikeCompletableObserver(compositeDisposable) {
                    override fun onComplete() {
                        product.isFavorite = false
                        getProducts()
                    }
                })
        } else {
            productRepository.addToFavorite(product).subscribeOn(Schedulers.io())
                .subscribe(object : NikeCompletableObserver(compositeDisposable) {
                    override fun onComplete() {
                        product.isFavorite = true
                        getProducts()
                    }
                })
        }

    }

    fun getProducts() {
        //Product List
        productRepository.getProducts(SORT_LATEST)
            .zipWith(
                productRepository.getProducts(SORT_POPULAR),
                { p1, p2 -> mutableListOf<Product>().apply { addAll(p1 + p2) } })
            .asyncNetworkRequest().doFinally { progressBarLiveData.value = false }
            .subscribe(object : NikeSingleObserver<MutableList<Product>>(compositeDisposable) {
                override fun onSuccess(t: MutableList<Product>) {
                    _latestProductLivaData.value = t.subList(0, t.size / 2)
                    _popularProductLivaData.value = t.subList(t.size / 2, t.size)
                }
            })
    }
}