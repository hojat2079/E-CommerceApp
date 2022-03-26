package com.application.nikestore.data.repo

import com.application.nikestore.data.entity.Comment
import io.reactivex.Single

interface CommentRepository {
    fun getAll(productId: Int): Single<List<Comment>>

    fun insert(): Single<Comment>
}