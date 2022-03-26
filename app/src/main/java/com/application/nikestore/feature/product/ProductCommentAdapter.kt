package com.application.nikestore.feature.product

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.application.nikestore.data.entity.Comment
import com.application.nikestore.databinding.ItemCommentBinding

class ProductCommentAdapter(private val showAll: Boolean) :
    RecyclerView.Adapter<ProductCommentAdapter.ViewHolder>() {
    var comments = ArrayList<Comment>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    inner class ViewHolder(private val bind: ItemCommentBinding) :
        RecyclerView.ViewHolder(bind.root) {
        fun onBindComment(comment: Comment) {
            bind.comment = comment
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemCommentBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.onBindComment(comments[position])
    }

    override fun getItemCount(): Int = if (comments.size > 3 && !showAll) 3
    else comments.size
}