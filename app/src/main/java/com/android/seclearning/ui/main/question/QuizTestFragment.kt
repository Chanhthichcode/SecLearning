package com.android.seclearning.ui.main.question

import android.os.Bundle
import android.view.LayoutInflater
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.seclearning.common.utils.marginWithStatusBar
import com.android.seclearning.common.utils.postDelayedSkipException
import com.android.seclearning.common.utils.setSafeOnClickListener
import com.android.seclearning.databinding.FragmentQuizQuestionBinding
import com.android.seclearning.ui.common.base.BaseFragment
import com.android.seclearning.ui.main.question.adapter.QuestionAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class QuizTestFragment : BaseFragment<FragmentQuizQuestionBinding>() {
    private val viewModel: QuestionViewModel by viewModels()
    private var questionAdapter: QuestionAdapter? = null
    override fun makeBinding(inflater: LayoutInflater): FragmentQuizQuestionBinding {
        return FragmentQuizQuestionBinding.inflate(inflater)
    }

    override fun initViewAndData(
        saveInstanceState: Bundle?,
        binding: FragmentQuizQuestionBinding
    ) {
        binding.spaceStatusBar.marginWithStatusBar()

        binding.btnBack.setSafeOnClickListener {
        }

        setupAdapter()
        observeData()

        viewModel.fetchDataQuestion()
    }

    private fun setupAdapter() {
        questionAdapter = QuestionAdapter().apply {
            onOptionSelected { questionId, optionIndex ->

            }
        }

        viewBinding()?.apply {
            rcQuiz.apply {
                adapter = questionAdapter
                layoutManager = LinearLayoutManager(
                    root.context,
                    LinearLayoutManager.VERTICAL,
                    false
                )
                setHasFixedSize(true)
            }
        }
    }

    private fun observeData() {
        lifecycleScope.launch {
            viewModel.listQuestion.collect { list ->
                questionAdapter?.setData(list)
            }
        }

        viewModel.fetchLoadingStatus.observe(this) { isLoading ->
            viewBinding()?.apply {
                if (isLoading) {
                    loading.startLoading()
                } else {
                    postDelayedSkipException(500) {
                        loading.stopLoading()
                    }
                }
            }
        }
    }
}