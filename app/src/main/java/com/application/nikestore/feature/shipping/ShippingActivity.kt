package com.application.nikestore.feature.shipping

import android.content.Intent
import android.os.Bundle
import android.view.View
import com.application.nikestore.R
import com.application.nikestore.common.*
import com.application.nikestore.data.entity.PurchaseDetail
import com.application.nikestore.data.entity.SubmitOrderResult
import com.application.nikestore.databinding.ActivityShippingBinding
import com.application.nikestore.feature.cart.CartItemAdapter
import com.application.nikestore.feature.checkout.CheckOutActivity
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import org.koin.android.viewmodel.ext.android.viewModel
import java.lang.IllegalStateException

class ShippingActivity : NikeActivity() {
    lateinit var binding: ActivityShippingBinding
    val viewModel: ShippingViewModel by viewModel()
    val compositeDisposable = CompositeDisposable()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityShippingBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val purchaseDetail = intent.extras!!.getParcelable<PurchaseDetail>(EXTRA_KEYS_DATA)
            ?: throw IllegalStateException("purchase detail cannot be null")
        val viewHolder = CartItemAdapter.PurchaseDetailViewHolder(binding.purchaseDetailView)
        viewHolder.bind(purchaseDetail)

        val onClickLister = View.OnClickListener { v ->
            viewModel.submitOrder(
                binding.shippingListFirstNameEt.text.toString(),
                binding.shippingListLastNameEt.text.toString(),
                binding.shippingZipCodeNameEt.text.toString(),
                binding.shippingListMobileNumberEt.text.toString(),
                binding.shippingListAddressEt.text.toString(),
                if (v.id == R.id.codBtn) METHOD_CASH_ON_DELIVERY else METHOD_ONLINE_PAYMENT
            ).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(
                object : NikeSingleObserver<SubmitOrderResult>(compositeDisposable) {
                    override fun onSuccess(t: SubmitOrderResult) {
                        if (t.bank_gateway_url.isNotEmpty()) {
                            openUrlInCustomTab(
                                this@ShippingActivity,
                                t.bank_gateway_url
                            )
                        } else {
                            startActivity(
                                Intent(
                                    this@ShippingActivity,
                                    CheckOutActivity::class.java
                                ).apply {
                                    putExtra(EXTRA_KEYS_ID, t.order_id)
                                }
                            )
                        }
                        finish()
                    }
                })
        }
        binding.codBtn.setOnClickListener(onClickLister)
        binding.onlinePaymentBtn.setOnClickListener(onClickLister)
    }

    override fun onDestroy() {
        super.onDestroy()
        compositeDisposable.clear()
    }
}