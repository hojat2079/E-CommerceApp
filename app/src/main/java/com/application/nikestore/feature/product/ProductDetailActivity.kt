package com.application.nikestore.feature.product

import android.content.Intent
import android.graphics.Paint
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.application.nikestore.R
import com.application.nikestore.common.*
import com.application.nikestore.data.entity.Comment
import com.application.nikestore.databinding.ActivityProductDetailBinding
import com.application.nikestore.feature.main.MainActivity
import com.application.nikestore.feature.product.comment.CommentListActivity
import com.application.nikestore.view.scroll.ObservableScrollViewCallbacks
import com.application.nikestore.view.scroll.ScrollState
import com.google.android.material.card.MaterialCardView
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import org.koin.android.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class ProductDetailActivity : NikeActivity() {
    private val productDetailViewModel: ProductDetailViewModel by viewModel { parametersOf(intent.extras) }
    private lateinit var binding: ActivityProductDetailBinding
    private val compositeDisposable = CompositeDisposable()
    private lateinit var toolbar: MaterialCardView
    private lateinit var previousPriceProduct: TextView
    private lateinit var currentPriceProduct: TextView
    private lateinit var showAllCommentButton: Button
    private lateinit var commentsRecyclerView: RecyclerView
    private lateinit var productImageView: ImageView
    private lateinit var addToCartBtn: ExtendedFloatingActionButton
    private val commentAdapter: ProductCommentAdapter = ProductCommentAdapter(false)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProductDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //initialize views
        initView()

        productDetailViewModel.progressBarLiveData.observe(this) {
            setProgressBarIndicator(it)
        }

        productDetailViewModel.productLiveData.observe(this) {
            binding.product = it
        }


        //get Comment from repository
        productDetailViewModel.commentLiveData.observe(this) {
            commentAdapter.comments = it as ArrayList<Comment>
            //visibility  show all button comment by comment size
            showAllCommentButton.visibility = if (it.size > 3) View.VISIBLE else View.GONE
            showAllCommentButton.setOnClickListener {
                startActivity(Intent(this, CommentListActivity::class.java).apply {
                    putExtra(EXTRA_KEYS_ID, productDetailViewModel.productLiveData.value!!.id)
                })
            }

        }
        addToCartBtn.setOnClickListener {
            productDetailViewModel.addToCart().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : NikeCompletableObserver(compositeDisposable) {
                    override fun onComplete() {

                        showSnackBar(getString(R.string.successAddToCart),
                            actionTitle = "مشاهده سبد خرید",
                            action = {
                                startActivity(
                                    Intent(
                                        this@ProductDetailActivity,
                                        MainActivity::class.java
                                    ).apply {
                                        putExtra(START_CART_ACTIVITY,true)
                                    }
                                )
                            })
                    }

                })
        }

    }

    private fun initView() {

        //bind View
        onBind()

        parallaxToolbar()

        strikeTextView()

        setRecyclerView()
    }


    private fun onBind() {
        productImageView = binding.productImageView
        toolbar = binding.toolberView
        previousPriceProduct = binding.previousPriceProduct
        commentsRecyclerView = binding.commentsRecyclerView
        showAllCommentButton = binding.showAllCommentBtn
        addToCartBtn = binding.addToCartFab
        currentPriceProduct = binding.priceProduct
    }

    private fun parallaxToolbar() {
        productImageView.post {
            val height = productImageView.height + currentPriceProduct.height
            binding.observableScrollView.addScrollViewCallbacks(object :
                ObservableScrollViewCallbacks {
                override fun onScrollChanged(
                    scrollY: Int,
                    firstScroll: Boolean,
                    dragging: Boolean
                ) {
                    toolbar.alpha = scrollY.toFloat() / height.toFloat()
                    productImageView.translationY = scrollY.toFloat() / 2
                }

                override fun onDownMotionEvent() {
                }

                override fun onUpOrCancelMotionEvent(scrollState: ScrollState?) {
                }

            })
        }
    }

    private fun strikeTextView() {
        //strike previous price Text View
        previousPriceProduct.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG

    }

    private fun setRecyclerView() {
        commentsRecyclerView.isNestedScrollingEnabled = false
        commentsRecyclerView.layoutManager =
            LinearLayoutManager(applicationContext, RecyclerView.VERTICAL, false)
        commentsRecyclerView.adapter = commentAdapter

    }

    override fun onDestroy() {
        super.onDestroy()
        compositeDisposable.clear()
    }
}