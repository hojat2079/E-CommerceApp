package com.application.nikestore

import android.app.Application
import android.content.SharedPreferences
import android.os.Bundle
import androidx.room.Room
import androidx.room.RoomDatabase
import com.application.nikestore.data.db.AppDatabase
import com.application.nikestore.data.db.ProductDao
import com.application.nikestore.feature.auth.AuthViewModel
import com.application.nikestore.feature.common.ProductListAdapter
import com.application.nikestore.feature.list.ProductListViewModel
import com.application.nikestore.feature.home.HomeViewModel
import com.application.nikestore.feature.product.ProductDetailViewModel
import com.application.nikestore.feature.product.comment.CommentListViewModel
import com.application.nikestore.data.repo.*
import com.application.nikestore.data.repo.source.*
import com.application.nikestore.feature.cart.CartViewModel
import com.application.nikestore.feature.checkout.CheckOutViewModel
import com.application.nikestore.feature.favorite.FavoriteProductViewModel
import com.application.nikestore.feature.main.MainViewModel
import com.application.nikestore.feature.profile.ProfileFragment
import com.application.nikestore.feature.profile.ProfileViewModel
import com.application.nikestore.feature.shipping.ShippingViewModel
import com.application.nikestore.service.loadingimage.FrescoImageLoadingService
import com.application.nikestore.service.loadingimage.ImageLoadingService
import com.application.nikestore.service.http.createApiServiceInstance
import com.facebook.drawee.backends.pipeline.Fresco
import org.koin.android.ext.android.get
import org.koin.android.ext.koin.androidContext
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.core.context.startKoin
import org.koin.dsl.module
import timber.log.Timber

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())

        Fresco.initialize(this)

        val modules = module {
            single { createApiServiceInstance() }

            single<ImageLoadingService> { FrescoImageLoadingService() }

            single<SharedPreferences> {
                this@App.getSharedPreferences(
                    "app_settings",
                    MODE_PRIVATE
                )
            }
            single { Room.databaseBuilder(this@App, AppDatabase::class.java, "db_name").build() }
            factory<ProductRepository> {
                ProductRepositoryImpl(
                    ProductLocalDataSource(get<AppDatabase>().favoriteProductDAO()),
                    ProductRemoteDataSource(get())
                )
            }
            single<UserDataSource> { UserLocalDataSource(get()) }
            single<UserRepository> {
                UserRepositoryImpl(
                    UserRemoteDataSource(get()),
                    get()
                )
            }

            factory<BannerRepository> {
                BannerRepositoryImpl(BannerRemoteDataSource(get()))
            }

            factory<CommentRepository> {
                CommentRepositoryImpl(CommentRemoteDataSource(get()))
            }

            factory<CartRepository> { CartRepositoryImpl(CartRemoteDataSource(get())) }

            single<OrderRepository> { OrderRepositoryImpl(OrderRemoteDataSource(get())) }

            factory { (viewType: Int) -> ProductListAdapter(viewType) }


            //init view models

            viewModel { HomeViewModel(get(), get()) }
            viewModel { (bundle: Bundle) -> ProductDetailViewModel(bundle, get(), get()) }
            viewModel { (id: Int) -> CommentListViewModel(id, get()) }
            viewModel { (id: Int) -> ProductListViewModel(id, get()) }
            viewModel { AuthViewModel(get()) }
            viewModel { CartViewModel(get()) }
            viewModel { MainViewModel(get()) }
            viewModel { ShippingViewModel(get()) }
            viewModel { (orderId: Int) -> CheckOutViewModel(orderId, get()) }
            viewModel { ProfileViewModel(get()) }
            viewModel { FavoriteProductViewModel(get()) }

        }
        startKoin {
            androidContext(this@App)
            modules(modules)
        }
        val userRepository: UserRepository = get()
        userRepository.loadToken()
    }
}