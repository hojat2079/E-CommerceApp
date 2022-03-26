package com.application.nikestore.data.repo

import com.application.nikestore.data.entity.Banner
import com.application.nikestore.data.repo.source.BannerDataSource
import io.reactivex.Single

class BannerRepositoryImpl(private val dataSource: BannerDataSource) : BannerRepository {
    override fun getBanner(): Single<List<Banner>> = dataSource.getBanner()

}