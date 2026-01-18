package com.android.seclearning.ui.main.login

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import androidx.fragment.app.viewModels
import com.android.seclearning.Logger
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
                is ResultState.Loading -> showLoading(binding)

                is ResultState.Success -> handleLoginSuccess(binding, state)

                is ResultState.Error -> showError(binding, state)
            }
        }
    }

    private fun showLoading(binding: FragmentLogInBinding) {
        Logger.d("LoginUI", "🔄 Loading")
        binding.loading.visible()
        binding.tvError.apply {
            visible()
            text = "Đang tải, vui lòng đợi"
        }
    }

    private fun handleLoginSuccess(
        binding: FragmentLogInBinding,
        state: ResultState.Success<*>
    ) {
        Logger.d("LoginUI", "✅ Success: ${state.data}")

        binding.loading.gone()
        binding.tvError.gone()

        DoneDialog
            .newInstance("Đăng nhập thành công")
            .show(parentFragmentManager, DoneDialog.TAG)

        appRepository().setLoggedIn()

        navigateAfterLogin()
    }

    private fun navigateAfterLogin() {
        if (appRepository().isAdmin()) {
            NavigationManager.navigateToMain(parentFragmentManager)
        } else {
            NavigationManager.navigateToQuestion(parentFragmentManager)
        }
    }

    private fun showError(
        binding: FragmentLogInBinding,
        state: ResultState.Error
    ) {
        Logger.e(
            "LoginUI",
            "❌ Error | code=${state.code} | message=${state.exception.message}"
        )

        binding.loading.gone()
        binding.tvError.apply {
            visible()
            text = state.exception.message
        }
    }
}
