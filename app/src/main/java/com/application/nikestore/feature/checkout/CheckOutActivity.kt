package com.application.nikestore.feature.checkout

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.application.nikestore.R
import com.application.nikestore.common.EXTRA_KEYS_ID
import com.application.nikestore.common.NikeActivity
import com.application.nikestore.databinding.ActivityCheckOutBinding
import org.koin.android.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class CheckOutActivity : NikeActivity() {
    private val viewModel: CheckOutViewModel by viewModel {
        val uri: Uri? = intent.data
        if (uri != null)
            parametersOf(uri.getQueryParameter("order_id")!!.toInt())
        else
            parametersOf(intent.extras!!.getInt(EXTRA_KEYS_ID))
    }
    private lateinit var binding: ActivityCheckOutBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCheckOutBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel.checkOutLiveData.observe(this) {
            binding.checkout = it
        }
    }
}