package com.application.nikestore.data.repo

import com.application.nikestore.data.entity.Banner
import io.reactivex.Single

interface BannerRepository {
    fun getBanner(): Single<List<Banner>>
}