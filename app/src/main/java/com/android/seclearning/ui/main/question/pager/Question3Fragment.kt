package com.android.seclearning.ui.main.question.pager

import android.os.Bundle
import android.view.LayoutInflater
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.android.seclearning.R
import com.android.seclearning.common.EventHelper
import com.android.seclearning.common.utils.setSafeOnClickScaleEffect
import com.android.seclearning.common.utils.visible
import com.android.seclearning.data.enums.AnswerType
import com.android.seclearning.databinding.FragmentQuestion3Binding
import com.android.seclearning.ui.common.base.BaseFragment
import com.android.seclearning.ui.main.question.QuestionViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class Question3Fragment : BaseFragment<FragmentQuestion3Binding>() {
    private val viewModel: QuestionViewModel by activityViewModels()
    override fun makeBinding(inflater: LayoutInflater): FragmentQuestion3Binding {
        return FragmentQuestion3Binding.inflate(inflater)
    }

    override fun initViewAndData(
        saveInstanceState: Bundle?,
        binding: FragmentQuestion3Binding
    ) {
        lifecycleScope.launch {
            viewModel.selectedOption.collect { option ->
                option?.let {
                    when (it) {
                        AnswerType.BEGINNER -> setTitle(activity?.getString(R.string.text_answer_1))
                        AnswerType.LEARNER -> {
                            setTitle(activity?.getString(R.string.text_answer_2))
                            binding.layoutChooseProfession.visible()
                            binding.btnNext.alpha = 0.5f
                        }

                        AnswerType.IMPROVER -> setTitle(activity?.getString(R.string.text_answer_3))

                    }
                }
            }
        }

        binding.btnNext.setSafeOnClickScaleEffect {
            EventHelper.post(EventClickDone())
        }
    }

    private fun setTitle(string: String?) {
        viewBinding()?.tvTitle?.text = string
    }

    class EventClickDone
}