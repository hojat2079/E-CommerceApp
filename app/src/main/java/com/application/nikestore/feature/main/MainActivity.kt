package com.application.nikestore.feature.main

import android.os.Bundle
import androidx.lifecycle.LiveData
import androidx.navigation.NavController
import com.application.nikestore.R
import com.application.nikestore.common.NikeActivity
import com.application.nikestore.common.START_CART_ACTIVITY
import com.application.nikestore.common.convertDpToPixel
import com.application.nikestore.common.setupWithNavController
import com.application.nikestore.data.entity.CartItemCount
import com.application.nikestore.databinding.ActivityMainBinding
import com.google.android.material.badge.BadgeDrawable
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.color.MaterialColors
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import org.koin.android.viewmodel.ext.android.viewModel


class MainActivity : NikeActivity() {
    private var isRunCart: Boolean = false
    private var currentNavController: LiveData<NavController>? = null
    private lateinit var binding: ActivityMainBinding
    private val mainViewModel: MainViewModel by viewModel()
    private lateinit var bottomNavView: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        bottomNavView = binding.bottomNavigationViewMain
        isRunCart = intent.getBooleanExtra(START_CART_ACTIVITY, false)
        setContentView(binding.root)
        if (savedInstanceState == null) {
            setupBottomNavigationBar()
        } // Else, need to wait for onRestoreInstanceState
        if (isRunCart)
            startCartFragment()
    }

    private fun startCartFragment() {
        bottomNavView.selectedItemId=R.id.cart
        isRunCart = false
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        // Now that BottomNavigationBar has restored its instance state
        // and its selectedItemId, we can proceed with setting up the
        // BottomNavigationBar with Navigation
        setupBottomNavigationBar()
    }

    /**
     * Called on first creation and when restoring state.
     */
    private fun setupBottomNavigationBar() {

        val navGraphIds = listOf(R.navigation.home, R.navigation.cart, R.navigation.profile)

        // Setup the bottom navigation view with SubmitOrderResult list of navigation graphs
        val controller = bottomNavView.setupWithNavController(
            navGraphIds = navGraphIds,
            fragmentManager = supportFragmentManager,
            containerId = R.id.nav_host_container,
            intent = intent
        )

        currentNavController = controller
    }

    override fun onSupportNavigateUp(): Boolean {
        return currentNavController?.value?.navigateUp() ?: false
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun createBadgeCartItemCount(cartItemCount: CartItemCount) {
        val badge = bottomNavView.getOrCreateBadge(R.id.cart)
        badge.apply {
            badgeGravity = BadgeDrawable.TOP_START
            isVisible = cartItemCount.count > 0
            number = cartItemCount.count
            verticalOffset = convertDpToPixel(10f, this@MainActivity).toInt()
            backgroundColor = MaterialColors.getColor(bottomNavView, R.attr.colorPrimary)
        }
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    fun removeBadge(count: String){
        if (count.isEmpty())
        bottomNavView.removeBadge(R.id.cart)
    }

    override fun onResume() {
        super.onResume()
        mainViewModel.getCartItemCount()
    }


}