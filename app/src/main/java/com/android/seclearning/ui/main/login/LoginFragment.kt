package com.android.seclearning.ui.main.login

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import androidx.fragment.app.viewModels
import com.android.seclearning.appRepository
import com.android.seclearning.common.utils.addBounceAnim
import com.android.seclearning.common.utils.gone
import com.android.seclearning.common.utils.setSafeOnClickListener
import com.android.seclearning.common.utils.setupPasswordToggle
import com.android.seclearning.common.utils.visible
import com.android.seclearning.databinding.FragmentLogInBinding
import com.android.seclearning.ui.NavigationManager
import com.android.seclearning.ui.common.base.BaseFragment
import com.android.seclearning.ui.dialog.DoneDialog
import com.android.seclearning.ui.main.login.viewModel.LoginViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlin.getValue

@AndroidEntryPoint
class LoginFragment : BaseFragment<FragmentLogInBinding>() {
    private val viewModel: LoginViewModel by viewModels()

    override fun makeBinding(inflater: LayoutInflater): FragmentLogInBinding {
        return FragmentLogInBinding.inflate(inflater)
    }

    override fun initViewAndData(
        saveInstanceState: Bundle?,
        binding: FragmentLogInBinding
    ) {
        appRepository().setFirstOpenAppToFalse()

        binding.etPassword.setupPasswordToggle()

        setupListener()

        setupObserver(binding)
    }

    private fun setupListener() {
        viewBinding()?.apply {
            btnRegister.setSafeOnClickListener {
                NavigationManager.navigateToRegister(parentFragmentManager)
            }

            btnLogIn.addBounceAnim()
            btnLogIn.setSafeOnClickListener {
                val email = etEmail.text.toString()
                val password = etPassword.text.toString()

                viewModel.login(email, password)
            }
        }
    }

    private fun setupObserver(binding: FragmentLogInBinding) {
        viewModel.loginState.observe(viewLifecycleOwner) { state ->
            when (state) {
                is ResultState.Loading -> {
                }

                is ResultState.Success -> {
                    binding.tvError.gone()
                    val dialog = DoneDialog()
                    dialog.show(parentFragmentManager, DoneDialog.TAG)
                    Handler(Looper.getMainLooper()).postDelayed({
                        dialog.dismiss()
                    }, 2500)
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