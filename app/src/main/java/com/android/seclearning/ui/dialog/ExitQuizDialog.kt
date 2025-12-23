package com.android.seclearning.ui.dialog

import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import com.android.seclearning.common.utils.setSafeOnClickListener
import com.android.seclearning.databinding.ExitQuizDialogBinding
import com.android.seclearning.ui.common.base.BaseDialog

class ExitQuizDialog : BaseDialog<ExitQuizDialogBinding>() {

    private var onClickConfirm: (() -> Unit)? = null

    fun onClickConfirm(onClickConfirm: () -> Unit) {
        this.onClickConfirm = onClickConfirm
    }

    override fun makeBinding(inflater: LayoutInflater): ExitQuizDialogBinding {
        return ExitQuizDialogBinding.inflate(inflater)
    }

    override fun getGravityForDialog(): Int {
        return Gravity.CENTER
    }

    override fun initViewAndData(
        saveInstanceState: Bundle?,
        binding: ExitQuizDialogBinding
    ) {
        this.isCancelable = true
        binding.root.setSafeOnClickListener {
            dismissDialog(TAG)
        }
        binding.btnConfirm.setSafeOnClickListener {
            onClickConfirm?.invoke()
            dismissDialog(TAG)
        }
        binding.btnCancel.setSafeOnClickListener {
            dismissDialog(TAG)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        onClickConfirm = null
    }

    companion object {
        const val TAG = "ExitAppDialog"
    }
}