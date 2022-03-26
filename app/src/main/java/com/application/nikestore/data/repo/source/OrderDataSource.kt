package com.application.nikestore.data.repo.source

import com.application.nikestore.data.entity.Checkout
import com.application.nikestore.data.entity.SubmitOrderResult
import io.reactivex.Single

interface OrderDataSource {
    fun submit(
        firstName: String,
        lastName: String,
        zipCode: String,
        phoneNumber: String,
        address: String,
        paymentMethod: String
    ): Single<SubmitOrderResult>

    fun checkout(orderId: Int): Single<Checkout>

}