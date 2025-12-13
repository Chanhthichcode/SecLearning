package com.android.seclearning.ui.main.question.pager

import android.os.Bundle
import android.view.LayoutInflater
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.android.seclearning.common.utils.setSafeOnClickScaleEffect
import com.android.seclearning.data.enums.AnswerType
import com.android.seclearning.databinding.FragmentQuestion2Binding
import com.android.seclearning.ui.common.base.BaseFragment
import com.android.seclearning.ui.main.question.QuestionViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class Question2Fragment : BaseFragment<FragmentQuestion2Binding>() {
    private val viewModel: QuestionViewModel by activityViewModels()
    override fun makeBinding(inflater: LayoutInflater): FragmentQuestion2Binding {
        return FragmentQuestion2Binding.inflate(inflater)
    }

    override fun initViewAndData(
        saveInstanceState: Bundle?,
        binding: FragmentQuestion2Binding
    ) {
        binding.btnAnswer1.setSafeOnClickScaleEffect {
            viewModel.setOption(AnswerType.BEGINNER)
            navigateToFragment3()
        }

        binding.btnAnswer2.setSafeOnClickScaleEffect {
            viewModel.setOption(AnswerType.LEARNER)
            navigateToFragment3()
        }

        binding.btnAnswer3.setSafeOnClickScaleEffect {
            viewModel.setOption(AnswerType.IMPROVER)
            navigateToFragment3()
        }
    }

    private fun navigateToFragment3() {
        lifecycleScope.launch {
            viewModel.goTo(2)
        }
    }
}