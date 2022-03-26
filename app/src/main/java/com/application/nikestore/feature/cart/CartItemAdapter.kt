package com.application.nikestore.feature.cart

import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.application.nikestore.data.entity.CartItem
import com.application.nikestore.data.entity.PurchaseDetail
import com.application.nikestore.databinding.*
import java.lang.IllegalStateException

const val VIEW_TYPE_CART_ITEM = 0
const val VIEW_TYPE_PURCHASE_DETAILS = 1

class CartItemAdapter(
    private val cartItems: MutableList<CartItem>,
    val callBack: CartItemCallBack
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    var purchaseDetail: PurchaseDetail? = null

    inner class CartItemsViewHolder(private val bind: ItemCartBinding) :
        RecyclerView.ViewHolder(bind.root) {
        fun onBind(cartItem: CartItem) {
            bind.cartItem = cartItem
            bind.previousPriceProductTv.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
            bind.progressBarChangeCount.visibility =
                if (cartItem.visibleProgressBarForChangeCount) View.VISIBLE else View.GONE

            bind.cartItemCountTv.visibility =
                if (cartItem.visibleProgressBarForChangeCount) View.INVISIBLE else View.VISIBLE

            bind.removeFromCartBtn.setOnClickListener {
                callBack.onRemoveCartItemButtonClick(cartItem)
            }
            bind.increaseBtn.setOnClickListener {
                if (cartItem.count<5){
                    cartItem.visibleProgressBarForChangeCount = true
                    bind.progressBarChangeCount.visibility = View.VISIBLE
                    bind.cartItemCountTv.visibility = View.INVISIBLE
                }
                    callBack.onIncreaseCartItemButtonClick(cartItem)
            }
            bind.decreaseBtn.setOnClickListener {
                if (cartItem.count > 1) {
                    cartItem.visibleProgressBarForChangeCount = true
                    bind.progressBarChangeCount.visibility = View.VISIBLE
                    bind.cartItemCountTv.visibility = View.INVISIBLE
                    callBack.onDecreaseCartItemButtonClick(cartItem)
                } else if (cartItem.count == 1) {
                    callBack.onRemoveCartItemButtonClick(cartItem)
                }
            }
            bind.productIv.setOnClickListener { callBack.onProductImageClick(cartItem) }
        }
    }

     class PurchaseDetailViewHolder(private val bind: ItemPurchaseDetailBinding) :
        RecyclerView.ViewHolder(bind.root) {
        fun bind(purchaseDetail: PurchaseDetail) {
            bind.purchaseDetail = purchaseDetail
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent.context)
        return when (viewType) {
            VIEW_TYPE_PURCHASE_DETAILS -> PurchaseDetailViewHolder(
                ItemPurchaseDetailBinding.inflate(
                    view, parent, false
                )
            )
            VIEW_TYPE_CART_ITEM -> CartItemsViewHolder(ItemCartBinding.inflate(view, parent, false))

            else -> throw IllegalStateException("invalid view Type")
        }
    }


    override fun getItemCount(): Int = cartItems.size + 1


    override fun getItemViewType(position: Int): Int {
        return if (cartItems.size == position)
            VIEW_TYPE_PURCHASE_DETAILS
        else VIEW_TYPE_CART_ITEM
    }


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is CartItemsViewHolder)
            holder.onBind(cartItems[position])
        else if (holder is PurchaseDetailViewHolder) {
            purchaseDetail?.let {
                holder.bind(it)
            }
        }
    }

    fun removeCartItem(cartItem: CartItem) {
        val index = cartItems.indexOf(cartItem)
        if (index > -1) {
            cartItems.removeAt(index)
            notifyItemRemoved(index)
        }
    }

    fun decreaseAndInCreaseCountCartItem(cartItem: CartItem) {
        val index = cartItems.indexOf(cartItem)
        if (index > -1) {
            cartItem.visibleProgressBarForChangeCount=false
            notifyItemChanged(index)
        }
    }


    interface CartItemCallBack {
        fun onRemoveCartItemButtonClick(cartItem: CartItem)

        fun onIncreaseCartItemButtonClick(cartItem: CartItem)

        fun onDecreaseCartItemButtonClick(cartItem: CartItem)

        fun onProductImageClick(cartItem: CartItem)


    }
}