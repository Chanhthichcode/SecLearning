package com.android.seclearning.ui.main.question.pager

import android.os.Bundle
import android.view.LayoutInflater
import androidx.fragment.app.activityViewModels
import com.android.seclearning.common.utils.setSafeOnClickScaleEffect
import com.android.seclearning.databinding.FragmentQuestion1Binding
import com.android.seclearning.ui.common.base.BaseFragment
import com.android.seclearning.ui.main.question.QuestionViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class Question1Fragment : BaseFragment<FragmentQuestion1Binding>() {
    private val viewModel: QuestionViewModel by activityViewModels()

    override fun makeBinding(inflater: LayoutInflater): FragmentQuestion1Binding {
        return FragmentQuestion1Binding.inflate(inflater)
    }

    override fun initViewAndData(
        saveInstanceState: Bundle?,
        binding: FragmentQuestion1Binding
    ) {
        binding.btnNext.setSafeOnClickScaleEffect {
            viewModel.goTo(1)
        }
    }
}