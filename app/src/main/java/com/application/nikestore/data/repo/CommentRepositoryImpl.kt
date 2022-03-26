package com.application.nikestore.data.repo

import com.application.nikestore.data.entity.Comment
import com.application.nikestore.data.repo.source.CommentDataSource
import io.reactivex.Single

class CommentRepositoryImpl(private val commentRemoteDataSource: CommentDataSource) :
    CommentRepository {
    override fun getAll(productId: Int): Single<List<Comment>> =
        commentRemoteDataSource.getAll(productId)

    override fun insert(): Single<Comment> {
        TODO("Not yet implemented")
    }
}