package com.application.nikestore.feature.home

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.application.nikestore.common.*
import com.application.nikestore.data.entity.Product
import com.application.nikestore.data.entity.SORT_LATEST
import com.application.nikestore.data.entity.SORT_POPULAR
import com.application.nikestore.databinding.FragmentHomeBinding
import com.application.nikestore.feature.common.ProductListAdapter
import com.application.nikestore.feature.common.VIEW_TYPE_ROUNDED
import com.application.nikestore.feature.list.ProductListActivity
import com.application.nikestore.feature.product.ProductDetailActivity
import org.koin.android.ext.android.inject
import org.koin.android.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import timber.log.Timber
import java.util.*
import kotlin.collections.ArrayList


class HomeFragment : NikeFragment(), ProductListAdapter.ProductEventListener {
    companion object {
        var timerForViewPager = false
    }

    private val homeViewModel: HomeViewModel by viewModel()
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private var bannerSliderAdapter: BannerSliderAdapter? = null
    private lateinit var latestProductRecyclerView: RecyclerView
    private lateinit var popularProductRecyclerView: RecyclerView
    private var timer: Timer? = null
    private lateinit var viewPager2: ViewPager2
    private val latestProductListAdapter: ProductListAdapter by inject {
        parametersOf(
            VIEW_TYPE_ROUNDED
        )
    }
    private val popularProductListAdapter: ProductListAdapter by inject {
        parametersOf(
            VIEW_TYPE_ROUNDED
        )
    }
    private var currentPositionSlider: Int = 0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewPager2 = binding.bannerSliderViewPager2

        //set listeners adapters
        latestProductListAdapter.onProductEventListener = this
        popularProductListAdapter.onProductEventListener = this

        //show item with latest
        latestProductRecyclerView = binding.latestProductRecyclerView
        latestProductRecyclerView.layoutManager =
            LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
        latestProductRecyclerView.adapter = latestProductListAdapter

        //show items with popular
        popularProductRecyclerView = binding.popularProductRecyclerView
        popularProductRecyclerView.layoutManager =
            LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
        popularProductRecyclerView.adapter = popularProductListAdapter

        homeViewModel.latestProductLivaData.observe(viewLifecycleOwner) {
            latestProductListAdapter.productList =ArrayList<Product>(it)
            Timber.i(it.toString())
        }
        homeViewModel.popularProductLivaData.observe(viewLifecycleOwner) {
            popularProductListAdapter.productList =ArrayList<Product>(it)
            Timber.i(it.toString())
        }

        homeViewModel.progressBarLiveData.observe(viewLifecycleOwner) {
            setProgressBarIndicator(it)
        }
        homeViewModel.bannerLivaData.observe(viewLifecycleOwner) {
            Timber.i(it.toString())
            bannerSliderAdapter = BannerSliderAdapter(this, it)
            viewPager2.adapter = bannerSliderAdapter
            binding.SliderIndicator.setViewPager2(viewPager2)
            if (!timerForViewPager && bannerSliderAdapter != null) {
                initSlider()
                timerForViewPager = true
            }

            viewPager2.post {
                val viewPagerHeight =
                    ((viewPager2.measuredWidth - convertDpToPixel(
                        32.toFloat(),
                        context
                    )) * 173) / 328
                val layoutParams = viewPager2.layoutParams
                layoutParams.height = viewPagerHeight.toInt()
                viewPager2.layoutParams = layoutParams
            }

        }
        binding.showAllLatestProductBtn.setOnClickListener {
            startActivity(Intent(requireContext(), ProductListActivity::class.java).apply {
                putExtra(EXTRA_KEYS_ID, SORT_LATEST)
            })
        }
        binding.showAllPopularProductBtn.setOnClickListener {
            startActivity(Intent(requireContext(), ProductListActivity::class.java).apply {
                putExtra(EXTRA_KEYS_ID, SORT_POPULAR)
            })
        }

        binding.searchEt.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s != null)
                    binding.closeIV.visibility = if (s.isEmpty()) View.GONE else View.VISIBLE
            }

            override fun afterTextChanged(s: Editable?) {
            }

        })
        binding.closeIV.setOnClickListener { binding.searchEt.setText("") }
    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }


    override fun onStop() {
        timer?.cancel()
        super.onStop()
    }

    override fun onResume() {
        //auto change view pager2 with timer
        if (timerForViewPager)
            initSlider()

        super.onResume()
        homeViewModel.getProducts()
    }

    override fun onClick(product: Product) {
        startActivity(Intent(requireContext(), ProductDetailActivity::class.java).apply {
            putExtra(EXTRA_KEYS_DATA, product)
        })
    }

    override fun favoriteBtnClick(product: Product) {
        homeViewModel.setFavoriteInProduct(product)
    }

    private fun initSlider() {
        if (bannerSliderAdapter != null) {
            timer = Timer()
            timer!!.schedule(object : TimerTask() {
                override fun run() {
                    activity?.runOnUiThread {
                        viewPager2.post {
                            viewPager2.currentItem =
                                currentPositionSlider % bannerSliderAdapter!!.itemCount
                            currentPositionSlider++
                        }
                    }
                }

            }, 1000, 4000) //changes 3 second you can change as per requirement
        }
    }


}