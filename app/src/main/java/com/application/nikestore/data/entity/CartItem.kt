package com.application.nikestore.data.entity

data class CartItem(
    val cart_item_id: Int,
    var count: Int,
    val product: Product,
    var visibleProgressBarForChangeCount: Boolean = false
)