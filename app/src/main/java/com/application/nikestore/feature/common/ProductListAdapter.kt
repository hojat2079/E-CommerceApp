package com.application.nikestore.feature.common

import android.graphics.Paint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.application.nikestore.R
import com.application.nikestore.common.implementSpringAnimationTrait
import com.application.nikestore.data.entity.Product
import com.application.nikestore.databinding.ItemProductBinding
import com.application.nikestore.databinding.ItemProductLargeBinding
import com.application.nikestore.databinding.ItemProductSmallBinding
import java.lang.IllegalStateException

const val VIEW_TYPE_ROUNDED = 0
const val VIEW_TYPE_SMALL = 1
const val VIEW_TYPE_LARGE = 2

class ProductListAdapter(var viewType: Int = VIEW_TYPE_ROUNDED) :
    RecyclerView.Adapter<ProductListAdapter.ViewHolder>() {
    var onProductEventListener: ProductEventListener? = null
    var productList = ArrayList<Product>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    inner class ViewHolder : RecyclerView.ViewHolder {
        private var itemProductBinding: ItemProductBinding? = null
        private var itemProductSmallBinding: ItemProductSmallBinding? = null
        private var itemProductLargeBinding: ItemProductLargeBinding? = null

        constructor(b: ItemProductBinding) : super(b.root) {
            itemProductBinding = b
        }

        constructor(b: ItemProductSmallBinding) : super(b.root) {
            itemProductSmallBinding = b
        }

        constructor(b: ItemProductLargeBinding) : super(b.root) {
            itemProductLargeBinding = b
        }

        fun onBind(product: Product) {
            if (viewType == VIEW_TYPE_LARGE) {
                val itemViewBind = itemProductLargeBinding!!.root
                itemProductLargeBinding!!.product = product
                itemProductLargeBinding!!.productPreviousPriceTv.paintFlags =
                    Paint.STRIKE_THRU_TEXT_FLAG
                itemViewBind.implementSpringAnimationTrait()
                itemViewBind.setOnClickListener {
                    onProductEventListener?.onClick(product)
                }
                if (product.isFavorite)
                    itemProductLargeBinding!!.favoriteBtn.setImageResource(R.drawable.ic_heart_filled_24)
                else itemProductLargeBinding!!.favoriteBtn.setImageResource(R.drawable.ic_heart_24)
                itemProductLargeBinding!!.favoriteBtn.setOnClickListener {
                    onProductEventListener?.favoriteBtnClick(product)
                    product.isFavorite = !product.isFavorite
                    notifyItemChanged(adapterPosition)
                }
            }
            if (viewType == VIEW_TYPE_SMALL) {
                val itemViewBind = itemProductSmallBinding!!.root
                itemProductSmallBinding!!.product = product
                itemProductSmallBinding!!.productPreviousPriceTv.paintFlags =
                    Paint.STRIKE_THRU_TEXT_FLAG
                itemViewBind.implementSpringAnimationTrait()
                itemViewBind.setOnClickListener {
                    onProductEventListener?.onClick(product)
                }
                if (product.isFavorite)
                    itemProductSmallBinding!!.favoriteBtn.setImageResource(R.drawable.ic_heart_filled_24)
                else itemProductSmallBinding!!.favoriteBtn.setImageResource(R.drawable.ic_heart_24)
                itemProductSmallBinding!!.favoriteBtn.setOnClickListener {
                    onProductEventListener?.favoriteBtnClick(product)
                    product.isFavorite = !product.isFavorite
                    notifyItemChanged(adapterPosition)
                }
            }
            if (viewType == VIEW_TYPE_ROUNDED) {
                val itemViewBind = itemProductBinding!!.root
                itemProductBinding!!.product = product
                itemProductBinding!!.productPreviousPriceTv.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
                itemViewBind.implementSpringAnimationTrait()
                itemViewBind.setOnClickListener {
                    onProductEventListener?.onClick(product)
                }
                if (product.isFavorite)
                    itemProductBinding!!.favoriteBtn.setImageResource(R.drawable.ic_heart_fill)
                else itemProductBinding!!.favoriteBtn.setImageResource(R.drawable.ic_heart)
                itemProductBinding!!.favoriteBtn.setOnClickListener {
                    onProductEventListener?.favoriteBtnClick(product)
//                    product.isFavorite = !product.isFavorite
//                    notifyItemChanged(adapterPosition)
                }
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
        return when (viewType) {
            VIEW_TYPE_LARGE -> ViewHolder(ItemProductLargeBinding.inflate(view, parent, false))
            VIEW_TYPE_SMALL -> ViewHolder(ItemProductSmallBinding.inflate(view, parent, false))
            VIEW_TYPE_ROUNDED -> ViewHolder(ItemProductBinding.inflate(view, parent, false))

            else -> throw IllegalStateException("invalid view Type")
        }

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) =
        holder.onBind(productList[position])


    override fun getItemCount(): Int = productList.size

    override fun getItemViewType(position: Int): Int = viewType

    interface ProductEventListener {
        fun onClick(product: Product)
        fun favoriteBtnClick(product: Product)
    }
}