package com.android.seclearning.ui.main.home.main.learning.pager

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.seclearning.R
import com.android.seclearning.databinding.FragmentResourceBinding
import com.android.seclearning.ui.common.base.BaseFragment
import com.android.seclearning.ui.main.home.main.learning.LearningViewModel
import com.android.seclearning.ui.main.home.main.learning.adapter.ItemResourceAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlin.getValue

@AndroidEntryPoint
class ResourceFragment : BaseFragment<FragmentResourceBinding>() {
    private val viewModel: LearningViewModel by activityViewModels()
    private var resourceAdapter: ItemResourceAdapter? = null

    override fun makeBinding(inflater: LayoutInflater): FragmentResourceBinding {
        return FragmentResourceBinding.inflate(inflater)
    }

    override fun initViewAndData(
        saveInstanceState: Bundle?,
        binding: FragmentResourceBinding
    ) {
        setupRecyclerView()
        observeViewModel()

        viewModel.loadResource()
    }

    private fun setupRecyclerView() {
        resourceAdapter = ItemResourceAdapter().apply {
            onCopy { text ->
                copyText("resource", text)
            }
        }
        viewBinding()?.rcLab?.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = resourceAdapter
            setHasFixedSize(true)
        }
    }


    private fun observeViewModel() {

        viewModel.resourceList.observe(this) { resource ->
            resourceAdapter?.submitList(resource)
        }

        viewModel.count.observe(this) { count ->
            viewBinding()?.tvCount?.text = "$count tài liệu"
        }

        viewModel.loading.observe(this) { isLoading ->
            viewBinding()?.loading?.visibility =
                if (isLoading) View.VISIBLE else View.GONE
        }
    }

    fun copyText(label: String, text: String) {
        viewModel.copyText(label, text)
        context.let { ctx ->
            Toast.makeText(ctx, getString(R.string.copied_to_clipboard), Toast.LENGTH_SHORT)
                .show()
        }
    }
}