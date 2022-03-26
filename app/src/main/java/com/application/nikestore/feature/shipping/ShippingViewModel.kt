package com.application.nikestore.feature.shipping

import com.application.nikestore.common.NikeViewModel
import com.application.nikestore.data.entity.SubmitOrderResult
import com.application.nikestore.data.repo.OrderRepository
import io.reactivex.Single

const val METHOD_CASH_ON_DELIVERY = "cash_on_delivery"
const val METHOD_ONLINE_PAYMENT = "online"

class ShippingViewModel(private val repository: OrderRepository) : NikeViewModel() {
    fun submitOrder(
        firstName: String,
        lastName: String,
        zipCode: String,
        phoneNumber: String,
        address: String,
        paymentMethod: String
    ): Single<SubmitOrderResult> {
        return repository.submit(firstName, lastName, zipCode, phoneNumber, address, paymentMethod)
    }
}