package com.application.nikestore.feature.product.comment

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.application.nikestore.common.EXTRA_KEYS_ID
import com.application.nikestore.common.NikeActivity
import com.application.nikestore.data.entity.Comment
import com.application.nikestore.databinding.ActivityListCommentBinding
import com.application.nikestore.feature.product.ProductCommentAdapter
import com.application.nikestore.view.NikeToolbar
import org.koin.android.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class CommentListActivity : NikeActivity() {
    private val commentListDataModel: CommentListViewModel by viewModel {
        parametersOf(intent.extras!!.getInt(EXTRA_KEYS_ID))
    }
    private lateinit var commentsRecyclerView: RecyclerView
    private lateinit var commentsAdapter: ProductCommentAdapter
    private lateinit var commentListToolbar: NikeToolbar
    lateinit var binding: ActivityListCommentBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityListCommentBinding.inflate(layoutInflater)
        setContentView(binding.root)
        commentsAdapter = ProductCommentAdapter(true)
        initView()
        //get comment in view model
        commentListDataModel.commentLiveData.observe(this) {
            commentsAdapter.comments = it as ArrayList<Comment>
            commentsRecyclerView.adapter = commentsAdapter
            commentsRecyclerView.layoutManager =
                LinearLayoutManager(applicationContext, RecyclerView.VERTICAL, false)
        }
        commentListDataModel.progressBarLiveData.observe(this) {
            setProgressBarIndicator(it)
        }

        //set onClick Listener for back Button
        commentListToolbar.onBackButtonClickListener = View.OnClickListener {
            finish()
        }

    }

    private fun initView() {
        commentsRecyclerView = binding.commentsRecyclerView
        commentListToolbar = binding.commentListToolbar
    }
}