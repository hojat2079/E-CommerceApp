package com.application.nikestore.service.loadingimage

import androidx.databinding.BindingAdapter
import com.application.nikestore.view.NikeImageView
import com.facebook.drawee.view.SimpleDraweeView
import java.lang.IllegalStateException

class FrescoImageLoadingService : ImageLoadingService {
    override fun loadImage(imageView: NikeImageView, url: String) {

    }

    companion object {
        @JvmStatic
        @BindingAdapter("loadImageWithUrl")
        fun NikeImageView.loadImageWithUrl(url: String) {
            if (this is SimpleDraweeView)
                this.setImageURI(url)
            else throw IllegalStateException("ImageView must be instance of SimpleDraweeView")
        }
    }

}