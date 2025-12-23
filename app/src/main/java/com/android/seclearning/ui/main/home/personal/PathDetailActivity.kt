package com.android.seclearning.ui.main.home.personal

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.seclearning.Logger
import com.android.seclearning.common.utils.marginWithStatusBar
import com.android.seclearning.common.utils.setSafeOnClickListener
import com.android.seclearning.databinding.ActivityPathDetailBinding
import com.android.seclearning.ui.common.base.BaseActivity
import com.android.seclearning.ui.main.home.personal.adapter.LessonAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PathDetailActivity : BaseActivity<ActivityPathDetailBinding>() {

    private val viewModel: PersonalViewModel by viewModels()
    private lateinit var lessonAdapter: LessonAdapter

    override fun makeBinding(inflater: LayoutInflater): ActivityPathDetailBinding {
        return ActivityPathDetailBinding.inflate(inflater)
    }

    override fun initViewAndData(
        saveInstanceState: Bundle?,
        binding: ActivityPathDetailBinding
    ) {
        // ===== Status bar =====
        binding.spaceStatusBar.marginWithStatusBar()

        // ===== Back =====
        binding.btnBack.setSafeOnClickListener { doFinish() }

        val career = intent.getStringExtra("career")
        if (career.isNullOrEmpty()) {
            return
        }
        viewModel.career = career
        Logger.d("PathDetail", "career = ${viewModel.career}")

        // ===== Setup UI =====
        setupRecyclerView(binding)
        observeData(binding)

        // ===== Call API =====
        viewModel.loaDetailProgress()
    }

    private fun setupRecyclerView(binding: ActivityPathDetailBinding) {
        lessonAdapter = LessonAdapter(viewModel.completed) { newCompleted ->
            viewModel.putProgress(itemIndex = newCompleted)
        }


        binding.rcQuiz.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = lessonAdapter
            setHasFixedSize(true)
        }
    }

    private fun observeData(binding: ActivityPathDetailBinding) {

        // ===== Loading =====
        viewModel.loading.observe(this) { isLoading ->
            binding.loading.visibility =
                if (isLoading) View.VISIBLE else View.GONE
        }

        // ===== Roadmap data =====
        viewModel.roadmap.observe(this) { response ->

            // Title
            binding.nameCategory.text = response.roadmap.name

            // Update completed
            lessonAdapter.updateCompleted(response.completed)

            // Submit list level
            lessonAdapter.submitList(response.roadmap.levels)
        }

        viewModel.putProgressResult.observe(this) { result ->

            result.onSuccess { newCompleted ->
                lessonAdapter.updateCompleted(newCompleted)
            }

            result.onFailure {
            }
        }
    }
}
