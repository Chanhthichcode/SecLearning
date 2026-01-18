package com.android.seclearning.ui.main.home.main.learning.pager

import android.os.Bundle
import android.view.LayoutInflater
import androidx.fragment.app.activityViewModels
import com.android.seclearning.common.utils.invisible
import com.android.seclearning.common.utils.visible
import com.android.seclearning.data.enums.OpenDetailFrom
import com.android.seclearning.databinding.FragmentOverviewBinding
import com.android.seclearning.ui.common.base.BaseFragment
import com.android.seclearning.ui.main.home.main.learning.LearningViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class OverviewFragment : BaseFragment<FragmentOverviewBinding>() {

    private val viewModel: LearningViewModel by activityViewModels()

    override fun makeBinding(inflater: LayoutInflater): FragmentOverviewBinding =
        FragmentOverviewBinding.inflate(inflater)

    override fun initViewAndData(
        saveInstanceState: Bundle?,
        binding: FragmentOverviewBinding
    ) {
        setupOverview(binding)
    }

    private fun setupOverview(binding: FragmentOverviewBinding) = with(binding) {
        hideAllOverview()

        when (viewModel.fromDetailPackage()) {
            OpenDetailFrom.SOC -> layoutSoc.root.visible()
            OpenDetailFrom.WEB -> layoutWeb.root.visible()
            OpenDetailFrom.NETWORK -> layoutNetwork.root.visible()
            OpenDetailFrom.MALWARE -> layoutMalware.root.visible()
            else -> layoutDifr.root.visible()
        }
    }

    private fun FragmentOverviewBinding.hideAllOverview() {
        layoutSoc.root.invisible()
        layoutWeb.root.invisible()
        layoutNetwork.root.invisible()
        layoutMalware.root.invisible()
        layoutDifr.root.invisible()
    }
}
