package com.application.nikestore.data.entity

import com.google.gson.annotations.SerializedName

data class AddToCartResponse(
    val id: Int,
    val product_id: Int,
    val count: Int
)
