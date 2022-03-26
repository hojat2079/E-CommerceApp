package com.application.nikestore.data.repo.source

import com.application.nikestore.data.entity.Comment
import com.application.nikestore.service.http.ApiService
import io.reactivex.Single

class CommentRemoteDataSource(private val apiService: ApiService) : CommentDataSource {
    override fun getAll(productId: Int): Single<List<Comment>> =
        apiService.getComment(productId)

    override fun insert(): Single<Comment> {
        TODO("Not yet implemented")
    }
}