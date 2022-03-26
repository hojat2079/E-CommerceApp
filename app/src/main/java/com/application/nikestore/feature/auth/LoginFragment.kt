package com.application.nikestore.feature.auth

import android.os.Bundle
import android.text.method.PasswordTransformationMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.application.nikestore.R
import com.application.nikestore.common.NikeCompletableObserver
import com.application.nikestore.databinding.FragmentLoginBinding
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import org.koin.android.viewmodel.ext.android.viewModel

class LoginFragment : Fragment() {
    private var compositeDisposable = CompositeDisposable()
    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!
    private lateinit var emailEt: EditText
    private lateinit var passwordEt: EditText
    private var isShowingPassword = false
    private val authViewModel: AuthViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        onBind()
        return binding.root
    }

    private fun onBind() {
        emailEt = binding.emailEt
        passwordEt = binding.passwordEt
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.loginBtn.setOnClickListener {
            authViewModel.login(emailEt.text.toString(), passwordEt.text.toString())
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(
                    object : NikeCompletableObserver(compositeDisposable) {
                        override fun onComplete() {
                            requireActivity().finish()
                                Toast.makeText(
                                    requireContext(),
                                    " خوش آمدید!${
                                        emailEt.text
                                    } ",
                                    Toast.LENGTH_SHORT
                                ).show()
                        }

                    }
                )
        }
        binding.registerLinkBtn.setOnClickListener {
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainer, RegisterFragment()).commit()
        }
        binding.isShowingPasswordIcon.setOnClickListener {
            isShowingPassword = !isShowingPassword
            if (isShowingPassword) {
                binding.isShowingPasswordIcon.setImageResource(R.drawable.ic_eye)
                passwordEt.transformationMethod = null
            } else {
                binding.isShowingPasswordIcon.setImageResource(R.drawable.ic_eye_off)
                passwordEt.transformationMethod = PasswordTransformationMethod()
            }
        }

    }

    override fun onStop() {
        super.onStop()
        compositeDisposable.clear()
    }
}