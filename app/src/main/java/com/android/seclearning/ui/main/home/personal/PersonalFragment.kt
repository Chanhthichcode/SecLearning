package com.android.seclearning.ui.main.home.personal

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.seclearning.data.enums.OpenLabFrom
import com.android.seclearning.databinding.FragmentPersonalBinding
import com.android.seclearning.ui.NavigationManager
import com.android.seclearning.ui.common.base.BaseFragment
import com.android.seclearning.ui.main.home.personal.adapter.ItemProgressAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlin.getValue

@AndroidEntryPoint
class PersonalFragment : BaseFragment<FragmentPersonalBinding>() {

    private val viewModel: PersonalViewModel by activityViewModels()
    private var progressAdapter : ItemProgressAdapter?= null

    override fun makeBinding(inflater: LayoutInflater): FragmentPersonalBinding {
        return FragmentPersonalBinding.inflate(inflater)
    }

    override fun initViewAndData(
        saveInstanceState: Bundle?,
        binding: FragmentPersonalBinding
    ) {

        setupRecyclerView()
        observeViewModel()

        viewModel.loadProgress()

    }

    private fun setupRecyclerView() {
        progressAdapter = ItemProgressAdapter().apply {
            onClickItem {
                activity?.run {
                    NavigationManager.navigateToPathDetailActivity(this, it)
                }
            }
        }
        viewBinding()?.rvProgress?.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = progressAdapter
            setHasFixedSize(true)
        }
    }


    private fun observeViewModel() {
        viewModel.progressList.observe(viewLifecycleOwner) { progress ->
            progressAdapter?.submitList(progress)

            val isEmpty = progress.isNullOrEmpty()
            viewBinding()?.rvProgress?.visibility = if (isEmpty) View.GONE else View.VISIBLE
            viewBinding()?.layoutEmpty?.visibility = if (isEmpty) View.VISIBLE else View.GONE
        }

        viewModel.loading.observe(viewLifecycleOwner) { isLoading ->
            viewBinding()?.loading?.visibility = if (isLoading) View.VISIBLE else View.GONE
        }
    }


    override fun onResume() {
        super.onResume()
        viewModel.loadProgress()
    }
}