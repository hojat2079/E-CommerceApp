package com.application.nikestore.feature.profile

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import com.application.nikestore.R
import com.application.nikestore.common.NikeFragment
import com.application.nikestore.databinding.FragmentProfileBinding
import com.application.nikestore.feature.auth.AuthActivity
import com.application.nikestore.feature.favorite.FavoriteProductActivity
import com.application.nikestore.feature.favorite.FavoriteProductAdapter
import org.koin.android.viewmodel.ext.android.viewModel

class ProfileFragment : NikeFragment() {
    private var _binding: FragmentProfileBinding? = null
    private val viewModel: ProfileViewModel by viewModel()
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.favoriteProductBtn.setOnClickListener {
            startActivity(Intent(requireContext(), FavoriteProductActivity::class.java))
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onResume() {
        super.onResume()
        checkAuthState()
    }

    private fun checkAuthState() {
        if (viewModel.isSigned) {
            binding.emailTv.text = viewModel.username
            binding.authTv.text = getString(R.string.singOut)
            binding.authTv.setTextColor(ContextCompat.getColor(requireContext(), R.color.red))
            binding.authTv.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_exit, 0)
            binding.authTv.setOnClickListener {
                viewModel.signOut(); checkAuthState()
            }

        } else {
            binding.emailTv.text = getString(R.string.guestUser)
            binding.authTv.text = getString(R.string.loginTitleTxt)
            binding.authTv.setTextColor(ContextCompat.getColor(requireContext(), R.color.blue))
            binding.authTv.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_login, 0)
            binding.authTv.setOnClickListener {
                startActivity(
                    Intent(requireContext(), AuthActivity::class.java)
                )
            }

        }
    }
}