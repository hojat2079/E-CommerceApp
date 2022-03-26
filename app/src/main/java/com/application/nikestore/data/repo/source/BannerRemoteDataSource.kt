package com.application.nikestore.data.repo.source

import com.application.nikestore.data.entity.Banner
import com.application.nikestore.service.http.ApiService
import io.reactivex.Single

class BannerRemoteDataSource(private val apiService: ApiService) : BannerDataSource {
    override fun getBanner(): Single<List<Banner>> = apiService.getBanner()

}