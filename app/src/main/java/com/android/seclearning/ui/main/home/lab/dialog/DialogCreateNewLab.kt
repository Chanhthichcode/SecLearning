package com.android.seclearning.ui.main.home.lab.dialog

import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import com.android.seclearning.common.utils.setSafeOnClickListener
import com.android.seclearning.databinding.DialogCreateNewLabBinding
import com.android.seclearning.ui.common.base.BaseDialog
import com.android.seclearning.ui.dialog.ExitQuizDialog

class DialogCreateNewLab : BaseDialog<DialogCreateNewLabBinding>() {
    private var onClickConfirm: ((String) -> Unit)? = null

    fun onClickConfirm(onClickConfirm: (String) -> Unit) {
        this.onClickConfirm = onClickConfirm
    }
    override fun makeBinding(inflater: LayoutInflater): DialogCreateNewLabBinding {
        return DialogCreateNewLabBinding.inflate(inflater)
    }

    override fun getGravityForDialog(): Int {
        return Gravity.BOTTOM
    }

    override fun initViewAndData(
        saveInstanceState: Bundle?,
        binding: DialogCreateNewLabBinding
    ) {
        this.isCancelable = true

        binding.root.setSafeOnClickListener {
            dismissDialog(ExitQuizDialog.TAG)
        }
        binding.btnSave.setSafeOnClickListener {
            val text = binding.tvLink.text.toString()
            if(text.isNotEmpty()) {
                onClickConfirm?.invoke(text)
            }
            dismissDialog(ExitQuizDialog.TAG)
        }
        binding.btnBack.setSafeOnClickListener {
            dismissDialog(ExitQuizDialog.TAG)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        onClickConfirm = null
    }

    companion object {
        const val TAG = "CreateLabDialog"
    }
}