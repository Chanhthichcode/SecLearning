package com.android.seclearning.ui.main.question.pager

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.android.seclearning.R
import com.android.seclearning.common.EventHelper
import com.android.seclearning.common.utils.disable
import com.android.seclearning.common.utils.enable
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
            viewModel.answerType.collect { option ->
                option?.let {
                    when (it) {
                        AnswerType.BEGINNER -> setTitle(activity?.getString(R.string.text_answer_1))
                        AnswerType.IMPROVER -> {
                            setTitle(activity?.getString(R.string.text_answer_3))
                            binding.layoutChooseProfession.visible()
                            binding.btnNext.alpha = 0.5f
                            binding.btnNext.disable()
                            setupProfessionSpinner()
                        }

                        AnswerType.LEARNER -> setTitle(activity?.getString(R.string.text_answer_2))

                    }
                }
            }
        }


        binding.btnNext.setSafeOnClickScaleEffect {
            val answerType = viewModel.answerType.value
                ?: return@setSafeOnClickScaleEffect

            EventHelper.post(EventClickDone(answerType))
        }

    }

    private fun setupProfessionSpinner() {
        val professionList = listOf(
            "Chọn lộ trình",
            "SOC Analyst / Blue Team",
            "DFIR Analyst (Digital Forensics)",
            "Malware Analyst",
            "Network Security / Red Team",
            "Web Pentester"
        )

        val adapter = object : ArrayAdapter<String>(
            requireContext(),
            android.R.layout.simple_spinner_item,
            professionList
        ) {
            override fun isEnabled(position: Int): Boolean {
                return position != 0
            }
        }

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        viewBinding()?.spinnerCity?.adapter = adapter

        viewBinding()?.spinnerCity?.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    if (position == 0) return

                    val selectedProfession = professionList[position]

                    viewModel.domain = selectedProfession

                    viewBinding()?.btnNext?.apply{
                        alpha = 1f
                        enable()
                    }
                }

                override fun onNothingSelected(parent: AdapterView<*>) {}
            }
    }


    private fun setTitle(string: String?) {
        viewBinding()?.tvTitle?.text = string
    }

    class EventClickDone(val answerType: AnswerType)
}