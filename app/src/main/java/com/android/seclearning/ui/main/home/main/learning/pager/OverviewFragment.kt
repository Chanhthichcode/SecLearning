package com.android.seclearning.ui.main.home.main.learning.pager

import android.os.Bundle
import android.view.LayoutInflater
import androidx.fragment.app.activityViewModels
import com.android.seclearning.common.utils.visible
import com.android.seclearning.data.enums.OpenDetailFrom
import com.android.seclearning.databinding.FragmentOverviewBinding
import com.android.seclearning.ui.common.base.BaseFragment
import com.android.seclearning.ui.main.home.main.learning.LearningViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlin.getValue

@AndroidEntryPoint
class OverviewFragment : BaseFragment<FragmentOverviewBinding>() {
    private val viewModel: LearningViewModel by activityViewModels()

    override fun makeBinding(inflater: LayoutInflater): FragmentOverviewBinding {
        return FragmentOverviewBinding.inflate(inflater)
    }

    override fun initViewAndData(
        saveInstanceState: Bundle?,
        binding: FragmentOverviewBinding
    ) {
        when (viewModel.fromDetailPackage()) {
            OpenDetailFrom.SOC -> {
                binding.layoutSoc.root.visible()
            }
            OpenDetailFrom.WEB -> {
                binding.layoutWeb.root.visible()
            }
            OpenDetailFrom.NETWORK -> {
                binding.layoutNetwork.root.visible()
            }
            OpenDetailFrom.MALWARE -> {
                binding.layoutMalware.root.visible()
            }
            else -> {
                binding.layoutDifr.root.visible()
            }
        }

    }
}