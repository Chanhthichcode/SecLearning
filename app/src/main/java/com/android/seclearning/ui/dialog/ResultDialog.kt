package com.android.seclearning.ui.dialog

import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import com.android.seclearning.common.utils.setSafeOnClickListener
import com.android.seclearning.data.enums.AnswerType
import com.android.seclearning.databinding.ResultDialogBinding
import com.android.seclearning.ui.common.base.BaseDialog

class ResultDialog : BaseDialog<ResultDialogBinding>() {

    private var type: AnswerType? = null
    private var questionTitle: String? = null
    private var quizPoint: Float? = null
    private var onConfirm: (() -> Unit)? = null


    fun setType(type: AnswerType): ResultDialog {
        this.type = type
        return this
    }

    fun setQuestionTitle(title: String?): ResultDialog {
        this.questionTitle = title
        return this
    }

    fun setQuizPoint(point: Float): ResultDialog {
        this.quizPoint = point
        return this
    }

    fun setOnConfirm(action: () -> Unit): ResultDialog {
        this.onConfirm = action
        return this
    }


    override fun makeBinding(inflater: LayoutInflater): ResultDialogBinding =
        ResultDialogBinding.inflate(inflater)

    override fun getGravityForDialog(): Int = Gravity.CENTER

    override fun initViewAndData(
        saveInstanceState: Bundle?,
        binding: ResultDialogBinding
    ) {
        isCancelable = false
        bindContent(binding)

        binding.btnNavigation.setSafeOnClickListener {
            onConfirm?.invoke()
            dismissDialog(TAG)
        }
    }


    private fun bindContent(binding: ResultDialogBinding) {
        when (type) {
            AnswerType.BEGINNER -> {
                binding.txtTitle.text =
                    "Lộ trình phù hợp với bạn: ${questionTitle.orEmpty()}"
                binding.txtDescription.text = ""
            }

            AnswerType.IMPROVER -> {
                binding.txtTitle.text =
                    "Điểm của bạn: ${quizPoint ?: 0f}"
                binding.txtDescription.text =
                    buildQuizDescription(quizPoint)
            }

            else -> {
                binding.txtTitle.text = ""
                binding.txtDescription.text = ""
            }
        }
    }

    private fun buildQuizDescription(point: Float?): String {
        val score = point ?: return ""
        return when {
            score <= 3f -> "Bạn sẽ bắt đầu từ giai đoạn 0"
            score <= 5f -> "Bạn sẽ bắt đầu từ giai đoạn 1"
            score <= 7f -> "Bạn sẽ bắt đầu từ giai đoạn 2"
            else -> "Bạn sẽ bắt đầu từ giai đoạn 3"
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        onConfirm = null
    }

    companion object {
        const val TAG = "ResultDialog"
    }
}
