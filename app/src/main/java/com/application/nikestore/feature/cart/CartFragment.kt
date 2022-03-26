package com.application.nikestore.feature.cart

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.application.nikestore.R
import com.application.nikestore.feature.auth.AuthActivity
import com.application.nikestore.common.EXTRA_KEYS_DATA
import com.application.nikestore.common.NikeCompletableObserver
import com.application.nikestore.common.NikeFragment
import com.application.nikestore.data.entity.CartItem
import com.application.nikestore.databinding.FragmentCartBinding
import com.application.nikestore.feature.product.ProductDetailActivity
import com.application.nikestore.feature.shipping.ShippingActivity
import com.google.android.material.button.MaterialButton
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import org.koin.android.viewmodel.ext.android.viewModel

class CartFragment : NikeFragment(), CartItemAdapter.CartItemCallBack {
    private val compositeDisposable = CompositeDisposable()
    private var _binding: FragmentCartBinding? = null
    private val binding get() = _binding!!
    private var cartItemAdapter: CartItemAdapter? = null
    private val cartViewModel: CartViewModel by viewModel()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCartBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        cartViewModel.progressBarLiveData.observe(viewLifecycleOwner) {
            setProgressBarIndicator(it)
        }

        cartViewModel.cartItemsLiveData.observe(viewLifecycleOwner) {
            if (it.isNotEmpty()) {
                binding.toolbarCart.visibility = View.VISIBLE
                binding.payBtn.visibility = View.VISIBLE
            }
            binding.cartItemRv.layoutManager =
                LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
            cartItemAdapter = CartItemAdapter(it as MutableList<CartItem>, this)
            binding.cartItemRv.adapter = cartItemAdapter
        }
        cartViewModel.purchaseDetailLiveData.observe(viewLifecycleOwner) {
            cartItemAdapter?.let { adapter ->
                adapter.purchaseDetail = it
                adapter.notifyItemChanged(adapter.itemCount - 1)
            }
        }
        cartViewModel.emptyStateLiveData.observe(viewLifecycleOwner) {
            val emptyStateView = showEmptyState(R.layout.view_cart_empty_state)
            if (it.mustShow) {

                emptyStateView?.let { emptyView ->
                    emptyView.findViewById<TextView>(R.id.titleEmptyState).text =
                        getString(it.messageResId)
                    val btnActionEmptyState =
                        emptyStateView.findViewById<MaterialButton>(R.id.actionBtnEmptyState)
                    btnActionEmptyState.visibility =
                        if (it.mustShowCallToActionButton) View.VISIBLE else View.GONE
                    btnActionEmptyState.setOnClickListener {
                        startActivity(Intent(requireContext(), AuthActivity::class.java))
                    }
                }
            } else emptyStateView?.let {
                emptyStateView.visibility = View.GONE
            }
        }
        binding.payBtn.setOnClickListener {
            startActivity(Intent(requireContext(), ShippingActivity::class.java).apply {
                putExtra(EXTRA_KEYS_DATA, cartViewModel.purchaseDetailLiveData.value)
            })
        }
    }

    override fun onStart() {
        binding.toolbarCart.visibility = View.GONE
        binding.payBtn.visibility = View.GONE
        cartViewModel.refresh()
        super.onStart()
    }

    override fun onRemoveCartItemButtonClick(cartItem: CartItem) {
        cartViewModel.removeItemFromCart(cartItem).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : NikeCompletableObserver(compositeDisposable) {
                override fun onComplete() {
                    cartItemAdapter?.removeCartItem(cartItem)
                }

            })

    }

    override fun onIncreaseCartItemButtonClick(cartItem: CartItem) {
        val temp=cartItem.count
        cartViewModel.increaseItemFromCart(cartItem).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : NikeCompletableObserver(compositeDisposable) {
                override fun onComplete() {
                    if (cartItem.count!=temp){ cartItemAdapter?.decreaseAndInCreaseCountCartItem(cartItem) }
                   else showSnackBar("تعداد محصول نمی تواند بیشتر از 5 عدد باشد")
                }

            })
    }

    override fun onDecreaseCartItemButtonClick(cartItem: CartItem) {
        cartViewModel.decreaseItemFromCart(cartItem).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : NikeCompletableObserver(compositeDisposable) {
                override fun onComplete() {
                    cartItemAdapter?.decreaseAndInCreaseCountCartItem(cartItem)
                }

            })
    }

    override fun onProductImageClick(cartItem: CartItem) {
        startActivity(Intent(requireContext(), ProductDetailActivity::class.java).apply {
            putExtra(EXTRA_KEYS_DATA, cartItem.product)
        })
    }

    override fun onStop() {
        super.onStop()
        compositeDisposable.clear()
    }
}