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
        observeAnswerType(binding)
        setupNextButton(binding)
    }

    private fun observeAnswerType(binding: FragmentQuestion3Binding) {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.answerType.collect { type ->
                type?.let { handleAnswerType(it, binding) }
            }
        }
    }

    private fun handleAnswerType(
        type: AnswerType,
        binding: FragmentQuestion3Binding
    ) {
        when (type) {
            AnswerType.BEGINNER -> {
                setTitle(getString(R.string.text_answer_1))
            }

            AnswerType.LEARNER -> {
                setTitle(getString(R.string.text_answer_2))
            }

            AnswerType.IMPROVER -> {
                setTitle(getString(R.string.text_answer_3))
                binding.layoutChooseProfession.visible()
                disableNextButton(binding)
                setupProfessionSpinner(binding)
            }
        }
    }

    private fun setupNextButton(binding: FragmentQuestion3Binding) {
        binding.btnNext.setSafeOnClickScaleEffect {
            val answerType = viewModel.answerType.value ?: return@setSafeOnClickScaleEffect
            EventHelper.post(EventClickDone(answerType))
        }
    }

    private fun disableNextButton(binding: FragmentQuestion3Binding) {
        binding.btnNext.apply {
            alpha = 0.5f
            disable()
        }
    }

    private fun enableNextButton(binding: FragmentQuestion3Binding) {
        binding.btnNext.apply {
            alpha = 1f
            enable()
        }
    }

    private fun getProfessionList(): List<String> {
        return listOf(
            "Chọn lộ trình",
            "SOC Analyst / Blue Team",
            "DFIR Analyst (Digital Forensics)",
            "Malware Analyst",
            "Network Security / Red Team",
            "Web Pentester"
        )
    }

    private fun createSpinnerAdapter(items: List<String>): ArrayAdapter<String> {
        return object : ArrayAdapter<String>(
            requireContext(),
            android.R.layout.simple_spinner_item,
            items
        ) {
            override fun isEnabled(position: Int): Boolean = position != 0
        }.apply {
            setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        }
    }

    private fun setupProfessionSpinner(binding: FragmentQuestion3Binding) {
        val professionList = getProfessionList()

        val adapter = createSpinnerAdapter(professionList)
        binding.spinnerCity.adapter = adapter

        binding.spinnerCity.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {

                override fun onItemSelected(
                    parent: AdapterView<*>,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    if (position == 0) return

                    viewModel.domain = professionList[position]
                    enableNextButton(binding)
                }

                override fun onNothingSelected(parent: AdapterView<*>) = Unit
            }
    }

    private fun setTitle(text: String?) {
        viewBinding()?.tvTitle?.text = text
    }

    class EventClickDone(val answerType: AnswerType)
}
