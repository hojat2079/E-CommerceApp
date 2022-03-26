package com.application.nikestore.feature.list

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.application.nikestore.R
import com.application.nikestore.common.EXTRA_KEYS_DATA
import com.application.nikestore.common.EXTRA_KEYS_ID
import com.application.nikestore.common.NikeActivity
import com.application.nikestore.data.entity.Product
import com.application.nikestore.databinding.ActivityProductListBinding
import com.application.nikestore.feature.common.ProductListAdapter
import com.application.nikestore.feature.common.VIEW_TYPE_LARGE
import com.application.nikestore.feature.common.VIEW_TYPE_SMALL
import com.application.nikestore.feature.product.ProductDetailActivity
import com.application.nikestore.view.NikeToolbar
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import org.koin.android.ext.android.inject
import org.koin.android.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import timber.log.Timber

class ProductListActivity : NikeActivity(), ProductListAdapter.ProductEventListener {
    private val productListViewModel: ProductListViewModel by viewModel {
        parametersOf(intent.extras!!.getInt(EXTRA_KEYS_ID))
    }
    private val productListAdapter: ProductListAdapter by inject { parametersOf(VIEW_TYPE_SMALL) }
    private lateinit var listProductRecyclerView: RecyclerView
    private lateinit var viewTypeChangerBtn: ImageView
    private lateinit var sortChangerBtn: View
    private lateinit var productListToolbar: NikeToolbar
    private lateinit var selectedSortedTitleTv: TextView
    private lateinit var bind: ActivityProductListBinding
    private lateinit var gridLayoutManager: GridLayoutManager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bind = ActivityProductListBinding.inflate(layoutInflater)
        setContentView(bind.root)

        onBindView()

        gridLayoutManager = GridLayoutManager(this, 2)
        listProductRecyclerView.layoutManager = gridLayoutManager
        listProductRecyclerView.adapter = productListAdapter
        productListAdapter.onProductEventListener = this

        productListViewModel.productsLiveData.observe(this) {
            productListAdapter.productList = it as ArrayList<Product>
        }
        productListViewModel.progressBarLiveData.observe(this) {
            setProgressBarIndicator(it)
        }
        viewTypeChangerBtn.setOnClickListener {
            if (productListAdapter.viewType == VIEW_TYPE_SMALL) {
                productListAdapter.viewType = VIEW_TYPE_LARGE
                gridLayoutManager.spanCount = 1
                viewTypeChangerBtn.setImageResource(R.drawable.ic_square)
            } else {
                productListAdapter.viewType = VIEW_TYPE_SMALL
                gridLayoutManager.spanCount = 2
                viewTypeChangerBtn.setImageResource(R.drawable.ic_grid)
            }
            productListAdapter.notifyDataSetChanged()
        }
        sortChangerBtn.setOnClickListener {
            MaterialAlertDialogBuilder(this)
                .setSingleChoiceItems(
                    R.array.sort, productListViewModel.sort
                ) { dialog, which ->
                    productListViewModel.changeSortItems(which)
                    dialog.dismiss()
                }
                .setTitle(R.string.sort)
                .show()
        }
        productListViewModel.selectedSortLiveData.observe(this) {
            Timber.i("sorted -> $it")
            selectedSortedTitleTv.text = getString(it)
        }
        productListToolbar.onBackButtonClickListener = View.OnClickListener { finish() }
    }

    private fun onBindView() {
        listProductRecyclerView = bind.productsListRecyclerView
        viewTypeChangerBtn = bind.viewTypeChangerBtn
        sortChangerBtn = bind.sortChangerBtn
        selectedSortedTitleTv = bind.selectedSortedTitleTv
        productListToolbar = bind.productListTitle
    }

    override fun onClick(product: Product) {
        startActivity(Intent(this, ProductDetailActivity::class.java).apply {
            putExtra(EXTRA_KEYS_DATA, product)
        })
    }

    override fun favoriteBtnClick(product: Product) {
        productListViewModel.setFavoriteInProduct(product)
    }
}