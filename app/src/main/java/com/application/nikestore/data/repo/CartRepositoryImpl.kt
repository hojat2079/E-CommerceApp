package com.application.nikestore.data.repo

import com.application.nikestore.data.entity.AddToCartResponse
import com.application.nikestore.data.entity.CartItemCount
import com.application.nikestore.data.entity.CartResponse
import com.application.nikestore.data.entity.MessageResponse
import com.application.nikestore.data.repo.source.CartDataSource
import io.reactivex.Single

class CartRepositoryImpl(private val remoteDataSource: CartDataSource) : CartRepository {
    override fun addToCart(productId: Int): Single<AddToCartResponse> =
        remoteDataSource.addToCart(productId)

    override fun getCartItems(): Single<CartResponse> = remoteDataSource.getCartItems()

    override fun removeItem(cartItemId: Int): Single<MessageResponse> =
        remoteDataSource.removeItem(cartItemId)

    override fun changeCountItem(cartItemId: Int, count: Int): Single<AddToCartResponse> {
        return remoteDataSource.changeCountItem(cartItemId, count)
    }

    override fun getCartItemsCount(): Single<CartItemCount> = remoteDataSource.getCartItemsCount()
}