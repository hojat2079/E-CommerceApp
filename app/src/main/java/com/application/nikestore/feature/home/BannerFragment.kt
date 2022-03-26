package com.application.nikestore.feature.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.application.nikestore.common.EXTRA_KEYS_DATA
import com.application.nikestore.data.entity.Banner
import com.application.nikestore.databinding.FragmentBannerBinding

class BannerFragment : Fragment() {
    private var _binding: FragmentBannerBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBannerBinding.inflate(inflater, container, false)
        val banner = requireArguments().getParcelable<Banner>(EXTRA_KEYS_DATA)
            ?: throw IllegalAccessError("Banner con nrt be null")
        binding.dataModelBanner = banner
        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    companion object {
        fun newInstance(banner: Banner): BannerFragment {
            return BannerFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(EXTRA_KEYS_DATA, banner)
                }
            }
        }
    }
}