package com.application.nikestore.feature.cart

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.application.nikestore.R
import com.application.nikestore.common.NikeSingleObserver
import com.application.nikestore.common.NikeViewModel
import com.application.nikestore.common.asyncNetworkRequest
import com.application.nikestore.data.common.TokenContainer
import com.application.nikestore.data.entity.*
import com.application.nikestore.data.repo.CartRepository
import io.reactivex.Completable
import org.greenrobot.eventbus.EventBus
import timber.log.Timber

class CartViewModel
    (
    private val cartRepository: CartRepository
) : NikeViewModel() {
    private val _cartItemsLiveData = MutableLiveData<List<CartItem>>()
    val cartItemsLiveData: LiveData<List<CartItem>> get() = _cartItemsLiveData
    private val _purchaseDetailLiveData = MutableLiveData<PurchaseDetail>()
    val purchaseDetailLiveData: LiveData<PurchaseDetail> get() = _purchaseDetailLiveData
    private val _emptyStateLiveData = MutableLiveData<EmptyState>()
    val emptyStateLiveData: LiveData<EmptyState> get() = _emptyStateLiveData

    private fun getCartItem() {
        if (!TokenContainer.token.isNullOrEmpty()) {
            _emptyStateLiveData.value = EmptyState(false)
            progressBarLiveData.value = true
            cartRepository.getCartItems().asyncNetworkRequest()
                .doFinally { progressBarLiveData.value = false }
                .subscribe(object : NikeSingleObserver<CartResponse>(compositeDisposable) {
                    override fun onSuccess(t: CartResponse) {
                        if (t.cart_items.isNotEmpty()) {
                            _cartItemsLiveData.value = t.cart_items
                            _purchaseDetailLiveData.value =
                                PurchaseDetail(t.total_price, t.shipping_cost, t.payable_price)
                        } else {
                            _emptyStateLiveData.value =
                                EmptyState(true, R.string.titleEmptyStateIsLogin)
                        }
                    }
                })
        } else {
            _emptyStateLiveData.value =
                EmptyState(
                    true, R.string.titleEmptyStateNoLogin,
                    true
                )
        }
    }

    fun removeItemFromCart(cartItem: CartItem): Completable {
        return cartRepository.removeItem(cartItem.cart_item_id)
            .doAfterSuccess {
                Timber.i("count item cartItems -> ${cartItemsLiveData.value?.size}")
                calculateAndPublishPurchaseDetail()
                cartItemsLiveData.value?.let {
                    if (it.isEmpty()) {
                        _emptyStateLiveData.postValue(
                            EmptyState(
                                true,
                                R.string.titleEmptyStateIsLogin
                            )
                        )
                    }
                }
                val cartItemCount = EventBus.getDefault().getStickyEvent(CartItemCount::class.java)
                cartItemCount?.let {
                    it.count -= cartItem.count
                    EventBus.getDefault().postSticky(cartItemCount)
                }
            }
            .ignoreElement()
    }

    fun increaseItemFromCart(cartItem: CartItem): Completable {
        val temp=cartItem.count
        return cartRepository.changeCountItem(
            cartItem.cart_item_id,
            if (cartItem.count < 5) ++cartItem.count else cartItem.count
        )
            .doAfterSuccess {
                calculateAndPublishPurchaseDetail()
                val cartItemCount = EventBus.getDefault().getStickyEvent(CartItemCount::class.java)
                cartItemCount?.let {
                    if (temp!=cartItem.count) {
                        it.count += 1
                        EventBus.getDefault().postSticky(cartItemCount)
                    }
                }
            }
            .ignoreElement()
    }

    fun decreaseItemFromCart(cartItem: CartItem): Completable {

        return cartRepository.changeCountItem(cartItem.cart_item_id, --cartItem.count)
            .doAfterSuccess {
                calculateAndPublishPurchaseDetail()
                val cartItemCount = EventBus.getDefault().getStickyEvent(CartItemCount::class.java)
                cartItemCount?.let {
                    it.count -= 1
                    EventBus.getDefault().postSticky(cartItemCount)
                }

            }
            .ignoreElement()
    }

    fun refresh() {
        getCartItem()
    }

    private fun calculateAndPublishPurchaseDetail() {
        _cartItemsLiveData.value?.let { cartItems ->
            _purchaseDetailLiveData.value?.let { purchaseDetail ->
                var totalPrice = 0
                var payablePrice = 0
                cartItems.forEach {
                    totalPrice += it.product.price * it.count
                    payablePrice += (it.product.price - it.product.discount) * it.count
                }
                purchaseDetail.payable_price = payablePrice
                purchaseDetail.total_price = totalPrice
                _purchaseDetailLiveData.postValue(purchaseDetail)
            }
        }
    }
}