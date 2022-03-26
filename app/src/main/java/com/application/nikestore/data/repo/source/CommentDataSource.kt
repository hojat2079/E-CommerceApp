package com.application.nikestore.data.repo.source

import com.application.nikestore.data.entity.Comment
import io.reactivex.Single

interface CommentDataSource {
    fun getAll(productId: Int): Single<List<Comment>>

    fun insert(): Single<Comment>
}