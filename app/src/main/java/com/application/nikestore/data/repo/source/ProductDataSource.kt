package com.application.nikestore.data.repo.source

import com.application.nikestore.data.entity.Product
import io.reactivex.Completable
import io.reactivex.Single

interface ProductDataSource {
    fun getProducts(sort:Int): Single<List<Product>>

    fun getFavoriteProducts(): Single<List<Product>>

    fun addToFavorite(product: Product): Completable

    fun deleteFromFavorites(product: Product): Completable

}
