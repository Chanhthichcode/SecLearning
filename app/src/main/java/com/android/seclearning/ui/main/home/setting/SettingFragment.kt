package com.android.seclearning.ui.main.home.setting

import android.os.Bundle
import android.view.LayoutInflater
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.viewModels
import com.android.seclearning.R
import com.android.seclearning.databinding.FragmentSettingBinding
import com.android.seclearning.ui.common.base.BaseFragment
import com.android.seclearning.ui.main.login.LoginFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SettingFragment : BaseFragment<FragmentSettingBinding>() {
    private val viewModel: SettingViewModel by viewModels()

    override fun makeBinding(inflater: LayoutInflater): FragmentSettingBinding {
        return FragmentSettingBinding.inflate(layoutInflater)
    }

    override fun initViewAndData(
        saveInstanceState: Bundle?,
        binding: FragmentSettingBinding
    ) {
        binding.btnLogOut.setOnClickListener {
            binding.btnLogOut.setOnClickListener {

                viewModel.logout()

                navigateToLoginAndClearStack()
            }
        }
    }

    private fun navigateToLoginAndClearStack() {
        val fragmentManager = requireActivity().supportFragmentManager

        fragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)

        fragmentManager.beginTransaction()
            .replace(R.id.frame_main, LoginFragment())
            .commitAllowingStateLoss()
    }

}