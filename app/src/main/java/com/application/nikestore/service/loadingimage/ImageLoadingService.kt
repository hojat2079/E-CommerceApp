package com.application.nikestore.service.loadingimage

import com.application.nikestore.view.NikeImageView

interface ImageLoadingService {
        fun loadImage(imageView: NikeImageView, url: String)
}