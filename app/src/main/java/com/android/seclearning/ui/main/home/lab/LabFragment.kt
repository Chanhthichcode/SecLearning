package com.android.seclearning.ui.main.home.lab

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.fragment.app.activityViewModels
import com.android.seclearning.common.utils.setSafeOnClickScaleEffect
import com.android.seclearning.data.enums.OpenLabFrom
import com.android.seclearning.databinding.FragmentSearchBinding
import com.android.seclearning.ui.NavigationManager
import com.android.seclearning.ui.common.base.BaseFragment
import com.android.seclearning.ui.main.home.lab.dialog.DialogCreateNewLab
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LabFragment : BaseFragment<FragmentSearchBinding>() {

    private val viewModel: LabViewModel by activityViewModels()

    override fun makeBinding(inflater: LayoutInflater): FragmentSearchBinding =
        FragmentSearchBinding.inflate(inflater)

    override fun initViewAndData(
        saveInstanceState: Bundle?,
        binding: FragmentSearchBinding
    ) {
        with(binding) {
            setupNavigation(this)
            setupAddLab(this)
            setupObserver(this)
        }

        viewModel.checkTryHackMeAndHTB()
    }

    private fun setupNavigation(binding: FragmentSearchBinding) = with(binding) {
        layoutBlueTeam.setSafeOnClickScaleEffect {
            navigate(OpenLabFrom.BLUE_TEAM)
        }
        layoutSeedLabs.setSafeOnClickScaleEffect {
            navigate(OpenLabFrom.SEED_LAB)
        }
        layoutLabtainer.setSafeOnClickScaleEffect {
            navigate(OpenLabFrom.LABTAINER)
        }
        layoutPortSwigger.setSafeOnClickScaleEffect {
            navigate(OpenLabFrom.PORT_SWIGGER)
        }
        layoutCyberDefenders.setSafeOnClickScaleEffect {
            navigate(OpenLabFrom.CYBER)
        }
        layoutNewLab.setSafeOnClickScaleEffect {
            navigate(OpenLabFrom.NEW_LAB)
        }
    }

    private fun navigate(openLabFrom: OpenLabFrom) {
        activity?.let {
            NavigationManager.navigateToLabDetailActivity(it, openLabFrom)
        }
    }

    private fun setupAddLab(binding: FragmentSearchBinding) = with(binding) {
        if (viewModel.isAdmin()) {
            layoutAddLab.visibility = View.VISIBLE
            layoutAddLab.setSafeOnClickScaleEffect {
                showCreateLabDialog()
            }
        } else {
            layoutAddLab.visibility = View.GONE
        }
    }

    private fun showCreateLabDialog() {
        val dialog = DialogCreateNewLab()
        dialog.setOnConfirmListener { labLink ->
            viewModel.createLab(labLink)
        }
        dialog.show(parentFragmentManager, DialogCreateNewLab.TAG)
    }

    private fun setupObserver(binding: FragmentSearchBinding) {
        viewModel.newLabCount.observe(viewLifecycleOwner) { count ->
            binding.layoutNewLab.visibility =
                if (count > 0) View.VISIBLE else View.GONE
        }

        viewModel.newLab.observe(viewLifecycleOwner) {
            viewModel.checkTryHackMeAndHTB()
        }

        viewModel.loading.observe(viewLifecycleOwner) { isLoading ->
            binding.loading.visibility =
                if (isLoading) View.VISIBLE else View.GONE
        }
    }
}
