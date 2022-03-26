package com.application.nikestore.data.repo

import com.application.nikestore.data.entity.Product
import com.application.nikestore.data.repo.source.ProductDataSource
import io.reactivex.Completable
import io.reactivex.Single

class ProductRepositoryImpl(
    private val productLocalDataSource: ProductDataSource,
    private val productRemoteDataSource: ProductDataSource
) : ProductRepository {
    override fun getProducts(sort: Int): Single<List<Product>> =
        productLocalDataSource.getFavoriteProducts().flatMap { favoriteProducts ->
            productRemoteDataSource.getProducts(sort).doOnSuccess { products ->
                val favoriteProductIds = favoriteProducts.map { it.id }
                products.forEach { product ->
                    if (favoriteProductIds.contains(product.id))
                        product.isFavorite = true
                }
            }
        }

    override fun getFavoriteProducts(): Single<List<Product>> {
        return productLocalDataSource.getFavoriteProducts()
    }

    override fun addToFavorite(product: Product): Completable {
        return productLocalDataSource.addToFavorite(product)
    }

    override fun deleteFromFavorites(product: Product): Completable {
        return productLocalDataSource.deleteFromFavorites(product)
    }
}