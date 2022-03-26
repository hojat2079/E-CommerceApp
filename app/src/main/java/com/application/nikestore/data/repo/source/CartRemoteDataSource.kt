package com.application.nikestore.data.repo.source

import com.application.nikestore.data.entity.AddToCartResponse
import com.application.nikestore.data.entity.CartItemCount
import com.application.nikestore.data.entity.CartResponse
import com.application.nikestore.data.entity.MessageResponse
import com.application.nikestore.service.http.ApiService
import com.google.gson.JsonObject
import io.reactivex.Single

class CartRemoteDataSource(private val apiService: ApiService) : CartDataSource {
    override fun addToCart(productId: Int): Single<AddToCartResponse> = apiService.addToCart(
        JsonObject().apply {
            addProperty("product_id", productId)
        }
    )

    override fun getCartItems(): Single<CartResponse> = apiService.getCart()

    override fun removeItem(cartItemId: Int): Single<MessageResponse> {
        return apiService.removeItemFromCart(JsonObject().apply {
            addProperty("cart_item_id", cartItemId)
        })
    }

    override fun changeCountItem(cartItemId: Int, count: Int): Single<AddToCartResponse> {
        return apiService.changeCountItem(JsonObject().apply {
            addProperty("cart_item_id", cartItemId)
            addProperty("count", count)
        })
    }

    override fun getCartItemsCount(): Single<CartItemCount> = apiService.getCartItemCount()
}