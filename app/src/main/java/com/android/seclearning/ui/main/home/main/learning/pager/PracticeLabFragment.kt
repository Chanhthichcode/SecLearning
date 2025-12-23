package com.android.seclearning.ui.main.home.main.learning.pager

import android.os.Bundle
import android.view.LayoutInflater
import com.android.seclearning.databinding.FragmentPractiveLabBinding
import com.android.seclearning.ui.common.base.BaseFragment

class PracticeLabFragment : BaseFragment<FragmentPractiveLabBinding>() {
    override fun makeBinding(inflater: LayoutInflater): FragmentPractiveLabBinding {
        return FragmentPractiveLabBinding.inflate(inflater)
    }

    override fun initViewAndData(
        saveInstanceState: Bundle?,
        binding: FragmentPractiveLabBinding
    ) {
    }
}