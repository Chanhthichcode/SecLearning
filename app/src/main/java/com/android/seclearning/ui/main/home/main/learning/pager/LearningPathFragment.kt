package com.android.seclearning.ui.main.home.main.learning.pager

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import androidx.fragment.app.activityViewModels
import com.android.seclearning.R
import com.android.seclearning.common.utils.invisible
import com.android.seclearning.common.utils.setSafeOnClickScaleEffect
import com.android.seclearning.common.utils.visible
import com.android.seclearning.data.enums.OpenDetailFrom
import com.android.seclearning.databinding.FragmentLearningPathBinding
import com.android.seclearning.ui.common.base.BaseFragment
import com.android.seclearning.ui.main.home.main.learning.LearningViewModel

class LearningPathFragment : BaseFragment<FragmentLearningPathBinding>() {

    private val viewModel: LearningViewModel by activityViewModels()

    override fun makeBinding(inflater: LayoutInflater): FragmentLearningPathBinding =
        FragmentLearningPathBinding.inflate(inflater)

    override fun initViewAndData(
        saveInstanceState: Bundle?,
        binding: FragmentLearningPathBinding
    ) {
        with(binding) {
            setupRoadmap(this)
            setupObserver()
            setupRegisterButton(this)
        }
    }

    private fun setupRoadmap(binding: FragmentLearningPathBinding) = with(binding) {
        hideAllRoadmaps()

        when (viewModel.fromDetailPackage()) {
            OpenDetailFrom.SOC -> {
                layoutSoc.root.visible()
                setTitle("Lộ trình SOC Analyst")
            }
            OpenDetailFrom.WEB -> {
                layoutWeb.root.visible()
                setTitle("Lộ trình Web Pentester")
            }
            OpenDetailFrom.NETWORK -> {
                layoutNetwork.root.visible()
                setTitle("Lộ trình Network Security")
            }
            OpenDetailFrom.MALWARE -> {
                layoutMalware.root.visible()
                setTitle("Lộ trình Malware Analyst")
            }
            else -> {
                layoutDifr.root.visible()
                setTitle("Lộ trình Digital Forensics & Incident Response")
            }
        }
    }

    private fun FragmentLearningPathBinding.hideAllRoadmaps() {
        layoutSoc.root.invisible()
        layoutWeb.root.invisible()
        layoutNetwork.root.invisible()
        layoutMalware.root.invisible()
        layoutDifr.root.invisible()
    }

    private fun setupObserver() {
        viewModel.registerResult.observe(viewLifecycleOwner) { result ->
            result.onSuccess {
                setButtonState(isSelected = true)
            }.onFailure {
                setButtonState(isSelected = false)
            }
        }
    }

    private fun setupRegisterButton(binding: FragmentLearningPathBinding) = with(binding) {
        if (viewModel.isAdmin()) {
            btnRegister.invisible()
        } else {
            btnRegister.visible()
            btnRegister.setSafeOnClickScaleEffect {
                viewModel.registerRoadmap()
            }
        }
    }

    private fun setTitle(tvTitle: String) {
        viewBinding()?.tvTitle?.text = tvTitle
    }

    private fun setButtonState(isSelected: Boolean) {
        viewBinding()?.btnRegister?.apply {
            if (isSelected) {
                text = "Đã đăng ký"
                setTextColor(Color.BLACK)
                setBackgroundResource(R.drawable.bg_btn_selected)
            } else {
                text = "Đăng ký"
                setTextColor(Color.WHITE)
                setBackgroundResource(R.drawable.bg_btn_gradient)
            }
        }
    }
}
