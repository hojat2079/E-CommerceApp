package com.application.nikestore.view

import android.content.Context
import android.graphics.ColorMatrix
import android.graphics.ColorMatrixColorFilter
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import com.application.nikestore.R
import com.application.nikestore.databinding.ViewToolbarBinding


class NikeToolbar(context: Context, attrs: AttributeSet?) : FrameLayout(context, attrs) {
    private var bind: ViewToolbarBinding
    var onBackButtonClickListener: View.OnClickListener? = null
        set(value) {
            field = value
            bind.backBtn.setOnClickListener(onBackButtonClickListener)
        }

    init {
        inflate(context, R.layout.view_toolbar, this)
        bind = ViewToolbarBinding.bind(this)
        val image: ImageView = bind.backBtn
        val matrix = ColorMatrix()
        matrix.setSaturation(0f)

        val filter = ColorMatrixColorFilter(matrix)
        image.colorFilter = filter
        if (attrs != null) {
            val values = context.obtainStyledAttributes(attrs, R.styleable.NikeToolbar)
            val title = values.getString(R.styleable.NikeToolbar_nt_title)
            if (title != null && title.isNotEmpty())
                bind.titleToolbarTextView.text = title
            values.recycle()
        }
    }
}