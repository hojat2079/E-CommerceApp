package com.application.nikestore.data.repo.source

import com.application.nikestore.data.db.ProductDao
import com.application.nikestore.data.entity.Product
import io.reactivex.Completable
import io.reactivex.Single

class ProductLocalDataSource(private val dao: ProductDao) : ProductDataSource {
    override fun getProducts(sort: Int): Single<List<Product>> {
        TODO("Not yet implemented")
    }

    override fun getFavoriteProducts(): Single<List<Product>> {
        return dao.getFavoriteProducts()
    }

    override fun addToFavorite(product: Product): Completable {
       return dao.addToFavorite(product)
    }

    override fun deleteFromFavorites(product: Product): Completable {
       return dao.deleteFromFavorites(product)
    }
}