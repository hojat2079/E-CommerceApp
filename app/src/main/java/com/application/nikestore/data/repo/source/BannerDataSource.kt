package com.application.nikestore.data.repo.source

import com.application.nikestore.data.entity.Banner
import io.reactivex.Single

interface BannerDataSource {
    fun getBanner(): Single<List<Banner>>
}