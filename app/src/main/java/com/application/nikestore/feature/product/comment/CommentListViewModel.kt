package com.application.nikestore.feature.product.comment

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.application.nikestore.common.NikeSingleObserver
import com.application.nikestore.common.NikeViewModel
import com.application.nikestore.common.asyncNetworkRequest
import com.application.nikestore.data.entity.Comment
import com.application.nikestore.data.repo.CommentRepository

class CommentListViewModel(
    private val productId: Int,
    private val commentRepository: CommentRepository
) : NikeViewModel() {
    private val _commentLiveData = MutableLiveData<List<Comment>>()
    val commentLiveData: LiveData<List<Comment>> get() = _commentLiveData

    init {
        progressBarLiveData.value=true
        commentRepository.getAll(productId).asyncNetworkRequest()
            .doFinally { progressBarLiveData.value=false }.subscribe(
            object : NikeSingleObserver<List<Comment>>(compositeDisposable) {
                override fun onSuccess(t: List<Comment>) {
                    _commentLiveData.value = t
                }

            }
        )
    }
}