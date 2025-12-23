package com.android.seclearning.ui.main.home.lab

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.activity.viewModels
import androidx.fragment.app.activityViewModels
import com.android.seclearning.common.utils.setSafeOnClickListener
import com.android.seclearning.common.utils.setSafeOnClickScaleEffect
import com.android.seclearning.data.enums.OpenLabFrom
import com.android.seclearning.databinding.FragmentSearchBinding
import com.android.seclearning.ui.NavigationManager
import com.android.seclearning.ui.common.base.BaseFragment
import com.android.seclearning.ui.main.home.lab.dialog.DialogCreateNewLab
import dagger.hilt.android.AndroidEntryPoint
import kotlin.getValue

@AndroidEntryPoint
class LabFragment : BaseFragment<FragmentSearchBinding>() {
    private val viewModel: LabViewModel by activityViewModels()

    override fun makeBinding(inflater: LayoutInflater): FragmentSearchBinding {
        return FragmentSearchBinding.inflate(inflater)
    }

    override fun initViewAndData(
        saveInstanceState: Bundle?,
        binding: FragmentSearchBinding
    ) {
        binding.layoutBlueTeam.setSafeOnClickScaleEffect {
            activity?.run {
                NavigationManager.navigateToLabDetailActivity(this, OpenLabFrom.BLUE_TEAM)
            }
        }
        binding.layoutSeedLabs.setSafeOnClickScaleEffect {
            activity?.run {
                NavigationManager.navigateToLabDetailActivity(this, OpenLabFrom.SEED_LAB)
            }
        }
        binding.layoutLabtainer.setSafeOnClickScaleEffect {
            activity?.run {
                NavigationManager.navigateToLabDetailActivity(this, OpenLabFrom.LABTAINER)
            }
        }
        binding.layoutPortSwigger.setSafeOnClickScaleEffect {
            activity?.run {
                NavigationManager.navigateToLabDetailActivity(this, OpenLabFrom.PORT_SWIGGER)
            }
        }
        binding.layoutCyberDefenders.setSafeOnClickScaleEffect {
            activity?.run {
                NavigationManager.navigateToLabDetailActivity(this, OpenLabFrom.CYBER)
            }
        }
        binding.layoutHackTheBox.setSafeOnClickScaleEffect {
            activity?.run {
                NavigationManager.navigateToLabDetailActivity(this, OpenLabFrom.HACK_THE_BOX)
            }
        }
        if (viewModel.isAdmin()) {
            binding.layoutAddLab.visibility = View.VISIBLE
            binding.layoutAddLab.setSafeOnClickScaleEffect {
                val dialog = DialogCreateNewLab()
                dialog.onClickConfirm { labLink ->
                    viewModel.createLab(labLink)
                }
                dialog.show(parentFragmentManager, DialogCreateNewLab.TAG)
            }
        } else {
            binding.layoutAddLab.visibility = View.GONE
        }

        viewModel.tryHackMeCount.observe(viewLifecycleOwner) { count ->
            binding.layoutTryHackMe.visibility = if (count > 0) View.VISIBLE else View.GONE
        }

        viewModel.hackTheBoxCount.observe(viewLifecycleOwner) { count ->
            binding.layoutHackTheBox.visibility = if (count > 0) View.VISIBLE else View.GONE
        }

        viewModel.checkTryHackMeAndHTB()

        viewModel.newLab.observe(viewLifecycleOwner) { lab ->
            viewModel.checkTryHackMeAndHTB()
        }

        viewModel.loading.observe(this) { isLoading ->
            viewBinding()?.loading?.visibility =
                if (isLoading) View.VISIBLE else View.GONE
        }
    }
}