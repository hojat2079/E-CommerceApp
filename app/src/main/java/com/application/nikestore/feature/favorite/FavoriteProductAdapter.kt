package com.application.nikestore.feature.favorite

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.application.nikestore.data.entity.Product
import com.application.nikestore.databinding.ItemFavoriteProductBinding

class FavoriteProductAdapter(
    private val products: MutableList<Product>,
    private val eventListener: FavoriteProductEventListener
) : RecyclerView.Adapter<FavoriteProductAdapter.ViewHolder>() {
    inner class ViewHolder(private val bind: ItemFavoriteProductBinding) :
        RecyclerView.ViewHolder(bind.root) {
        fun bind(product: Product) {
            bind.product = product
            itemView.setOnClickListener {
                eventListener.onClick(product)
            }
            itemView.setOnLongClickListener {
                products.remove(product)
                notifyItemRemoved(adapterPosition)
                eventListener.onLongClick(product,products.size)
                false
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemFavoriteProductBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(products[position])
    }

    override fun getItemCount(): Int = products.size

    interface FavoriteProductEventListener {
        fun onClick(product: Product)
        fun onLongClick(product: Product,size:Int)
    }
}