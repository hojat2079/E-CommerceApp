package com.application.nikestore.data.entity

import androidx.annotation.StringRes

data class EmptyState(
    val mustShow: Boolean,
    @StringRes val messageResId: Int = 0,
    val mustShowCallToActionButton: Boolean = false
)