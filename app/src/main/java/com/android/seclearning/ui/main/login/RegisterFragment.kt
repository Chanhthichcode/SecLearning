package com.android.seclearning.ui.main.login

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import androidx.fragment.app.viewModels
import com.android.seclearning.common.utils.addBounceAnim
import com.android.seclearning.common.utils.gone
import com.android.seclearning.common.utils.setSafeOnClickListener
import com.android.seclearning.common.utils.setupPasswordToggle
import com.android.seclearning.common.utils.visible
import com.android.seclearning.databinding.FragmentRegisterBinding
import com.android.seclearning.ui.NavigationManager
import com.android.seclearning.ui.common.base.BaseFragment
import com.android.seclearning.ui.dialog.DoneDialog
import com.android.seclearning.ui.main.login.viewModel.LoginViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RegisterFragment : BaseFragment<FragmentRegisterBinding>() {
    private val viewModel: LoginViewModel by viewModels()

    override fun makeBinding(inflater: LayoutInflater): FragmentRegisterBinding {
        return FragmentRegisterBinding.inflate(inflater)
    }

    override fun initViewAndData(
        saveInstanceState: Bundle?,
        binding: FragmentRegisterBinding
    ) {
        binding.etPassword.setupPasswordToggle()
        binding.etConfirmPassword.setupPasswordToggle()

        setupListeners()
        setupObserver(binding)
    }

    private fun setupListeners() {
        viewBinding()?.apply {
            btnRegister.addBounceAnim()
            btnRegister.setSafeOnClickListener {
                val email = etEmail.text.toString()
                val username = etUsername.text.toString()
                val password = etPassword.text.toString()
                val rePassword = etConfirmPassword.text.toString()

                viewModel.register(email, username, password, rePassword)
            }

            btnLogin.setSafeOnClickListener {
                NavigationManager.navigateToLogin(parentFragmentManager)
            }
        }
    }

    private fun setupObserver(binding: FragmentRegisterBinding) {
        viewModel.registerState.observe(viewLifecycleOwner) { state ->
            when (state) {
                is ResultState.Loading -> {
                }

                is ResultState.Success -> {
                    binding.tvError.gone()
                    DoneDialog
                        .newInstance("Đăng ký thành công")
                        .show(parentFragmentManager, DoneDialog.TAG)
                    Handler(Looper.getMainLooper()).postDelayed({
                    }, 2500)


                    NavigationManager.navigateToLogin(parentFragmentManager)
                }

                is ResultState.Error -> {
                    binding.tvError.apply {
                        visible()
                        text = state.exception.message
                    }

                }
            }
        }
    }
}