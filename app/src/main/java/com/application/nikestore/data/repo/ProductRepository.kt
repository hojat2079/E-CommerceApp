package com.application.nikestore.data.repo

import com.application.nikestore.data.entity.Product
import io.reactivex.Completable
import io.reactivex.Single

interface ProductRepository {
    fun getProducts(sort:Int): Single<List<Product>>

    fun getFavoriteProducts(): Single<List<Product>>

    fun addToFavorite(product: Product): Completable

    fun deleteFromFavorites(product: Product): Completable
}