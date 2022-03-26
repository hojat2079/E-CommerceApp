package com.application.nikestore.feature.favorite

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.application.nikestore.R
import com.application.nikestore.common.EXTRA_KEYS_DATA
import com.application.nikestore.common.NikeActivity
import com.application.nikestore.data.entity.Product
import com.application.nikestore.databinding.ActivityFavoriteProductBinding
import com.application.nikestore.feature.product.ProductDetailActivity
import com.google.android.material.snackbar.Snackbar
import org.koin.android.viewmodel.ext.android.viewModel

class FavoriteProductActivity : NikeActivity(),
    FavoriteProductAdapter.FavoriteProductEventListener {
    lateinit var binding: ActivityFavoriteProductBinding
    private lateinit var recyclerView: RecyclerView
    val viewModel: FavoriteProductViewModel by viewModel()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFavoriteProductBinding.inflate(layoutInflater)

        setContentView(binding.root)
        recyclerView = binding.FavoriteProductRv
        viewModel.favoriteProductLiveData.observe(this) {
            recyclerView.adapter = FavoriteProductAdapter(it as MutableList<Product>, this)
            recyclerView.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
            if (it.isEmpty()) {
                showEmptyState(R.layout.view_favorite_emptey_state)
            }
        }
        binding.infoIv.setOnClickListener {
            showSnackBar(getString(R.string.deleteFromFavorites), Snackbar.LENGTH_LONG)
        }
    }

    override fun onClick(product: Product) {
        startActivity(Intent(this, ProductDetailActivity::class.java).apply {
            putExtra(EXTRA_KEYS_DATA, product)
        })
    }

    override fun onLongClick(product: Product, size: Int) {
        viewModel.removeFromFavorite(product,size)
    }
}