package com.android.seclearning.ui.main.question

import android.os.Bundle
import android.view.LayoutInflater
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.seclearning.Logger
import com.android.seclearning.appRepository
import com.android.seclearning.common.utils.marginWithStatusBar
import com.android.seclearning.common.utils.postDelayedSkipException
import com.android.seclearning.common.utils.setSafeOnClickListener
import com.android.seclearning.data.enums.AnswerType
import com.android.seclearning.databinding.FragmentQuizQuestionBinding
import com.android.seclearning.ui.NavigationManager
import com.android.seclearning.ui.common.base.BaseFragment
import com.android.seclearning.ui.dialog.ExitQuizDialog
import com.android.seclearning.ui.dialog.ResultDialog
import com.android.seclearning.ui.dialog.SubmitQuizDialog
import com.android.seclearning.ui.main.question.adapter.QuestionAdapter
import com.android.seclearning.ui.main.question.adapter.QuizAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class QuizTestFragment : BaseFragment<FragmentQuizQuestionBinding>() {

    private val viewModel: QuestionViewModel by activityViewModels()

    private var openResultAfterSubmit = false

    private var questionAdapter: QuestionAdapter? = null
    private var quizAdapter: QuizAdapter? = null

    /* ================= BASE ================= */

    override fun makeBinding(inflater: LayoutInflater): FragmentQuizQuestionBinding {
        return FragmentQuizQuestionBinding.inflate(inflater)
    }

    override fun initViewAndData(
        saveInstanceState: Bundle?,
        binding: FragmentQuizQuestionBinding
    ) {
        setupUI(binding)
        observeAnswerType()

    }

    /* ================= SETUP ================= */

    private fun setupUI(binding: FragmentQuizQuestionBinding) {
        binding.spaceStatusBar.marginWithStatusBar()

        binding.btnBack.setSafeOnClickListener {
            ExitQuizDialog().apply {
                onClickConfirm {
                    parentFragmentManager.popBackStack()
                }
            }.show(parentFragmentManager, ExitQuizDialog.TAG)
        }

        binding.btnSubmit.setSafeOnClickListener {
            SubmitQuizDialog().apply {
                onClickConfirm {
                    viewModel.answerType.value?.let {
                        viewModel.submitByAnswerType(it)
                        openResultAfterSubmit = true
                    }
                }
            }.show(parentFragmentManager, SubmitQuizDialog.TAG)
        }
    }

    /**
     * Quan sát AnswerType → quyết định setup UI + fetch data
     */
    private fun observeAnswerType() {
        lifecycleScope.launch {
            viewModel.answerType.collect { type ->
                type ?: return@collect

                setupAdapterByType(type)
                observeByType(type)
                viewModel.fetchDataByAnswerType(type)
            }
        }
    }

    private fun setupAdapterByType(type: AnswerType) {
        when (type) {
            AnswerType.BEGINNER -> setupQuestionAdapter()
            AnswerType.IMPROVER -> setupQuizAdapter()
            else -> Unit
        }
    }

    /* ================= ADAPTER ================= */

    private fun setupQuestionAdapter() {
        questionAdapter = QuestionAdapter().apply {
            onOptionSelected { questionId, optionIndex ->
                viewModel.updateAnswer(questionId, optionIndex)
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

    private fun setupQuizAdapter() {
        quizAdapter = QuizAdapter().apply {
            onOptionSelected { questionId, optionIndex ->
                viewModel.updateQuiz(questionId, optionIndex)
            }
        }

        viewBinding()?.apply {
            rcQuiz.apply {
                adapter = quizAdapter
                layoutManager = LinearLayoutManager(
                    root.context,
                    LinearLayoutManager.VERTICAL,
                    false
                )
                setHasFixedSize(true)
            }
        }
    }

    /* ================= OBSERVE ================= */

    private fun observeByType(type: AnswerType) {
        when (type) {
            AnswerType.BEGINNER -> observeQuestion()
            AnswerType.IMPROVER -> observeQuiz()
            else -> Unit
        }

        observeLoading()
    }

    private fun observeQuestion() {
        lifecycleScope.launch {
            viewModel.listQuestion.collect { list ->
                questionAdapter?.setData(list)
                updateSubmitState(
                    list.isNotEmpty() && list.all { it.selectedOption != -1 }
                )
            }
        }

        viewModel.submissionResult.observe(viewLifecycleOwner) { response ->
            openResultIfNeeded(response.result.firstOrNull().orEmpty())
        }
    }

    private fun observeQuiz() {
        lifecycleScope.launch {
            viewModel.listQuiz.collect { list ->
                quizAdapter?.setData(list)
                updateSubmitState(
                    list.isNotEmpty() && list.all { it.selectedOption != -1 }
                )
            }
        }

        viewModel.submissionQuiz.observe(viewLifecycleOwner) { response ->
            openResultIfNeeded(response.summary.points)
        }
    }

    private fun observeLoading() {
        viewModel.fetchLoadingStatus.observe(viewLifecycleOwner) { isLoading ->
            viewBinding()?.let {
                if (isLoading) {
                    it.loading.startLoading()
                } else {
                    postDelayedSkipException(500) {
                        it.loading.stopLoading()
                    }
                }
            }
        }
    }

    /* ================= RESULT ================= */

    private fun openResultIfNeeded(result: Any) {
        if (!openResultAfterSubmit) return
        openResultAfterSubmit = false

        viewModel.answerType.value?.let { type ->
            val dialog = ResultDialog().setType(type)

            when (result) {
                is String -> dialog.setQuestionTitle(result)
                is Float -> dialog.setQuizPoint(result)
            }

            dialog.setOnConfirm {
                appRepository().setBuildRoadmap()
                NavigationManager.navigateToMain(parentFragmentManager)
            }.show(parentFragmentManager, ResultDialog.TAG)
        }
    }

    /* ================= UI ================= */

    private fun updateSubmitState(enable: Boolean) {
        viewBinding()?.btnSubmit?.apply {
            isEnabled = enable
            alpha = if (enable) 1f else 0.5f
        }

        Logger.d("Quiz", "Submit enable = $enable")
    }
}
