package com.android.seclearning.ui.main.login

import android.os.Bundle
import android.view.LayoutInflater
import com.android.seclearning.databinding.FragmentForgotPasswordBinding
import com.android.seclearning.ui.common.base.BaseFragment

class ForgotPasswordFragment : BaseFragment<FragmentForgotPasswordBinding>(){
    override fun makeBinding(inflater: LayoutInflater): FragmentForgotPasswordBinding {
        return FragmentForgotPasswordBinding.inflate(layoutInflater)
    }

    override fun initViewAndData(
        saveInstanceState: Bundle?,
        binding: FragmentForgotPasswordBinding
    ) {

    }
}