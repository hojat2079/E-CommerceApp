package com.application.nikestore.data.repo

import com.application.nikestore.data.entity.AddToCartResponse
import com.application.nikestore.data.entity.CartItemCount
import com.application.nikestore.data.entity.CartResponse
import com.application.nikestore.data.entity.MessageResponse
import io.reactivex.Single

interface CartRepository {

    fun addToCart(productId: Int): Single<AddToCartResponse>

    fun getCartItems(): Single<CartResponse>

    fun removeItem(cartItemId: Int): Single<MessageResponse>

    fun changeCountItem(cartItemId: Int, count: Int): Single<AddToCartResponse>

    fun getCartItemsCount(): Single<CartItemCount>
}