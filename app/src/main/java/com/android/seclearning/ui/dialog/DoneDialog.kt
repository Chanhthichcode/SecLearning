package com.android.seclearning.ui.dialog

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import com.android.seclearning.databinding.DoneDialogBinding
import com.android.seclearning.ui.common.base.BaseDialog

class DoneDialog : BaseDialog<DoneDialogBinding>() {

    override fun makeBinding(inflater: LayoutInflater): DoneDialogBinding {
        return DoneDialogBinding.inflate(inflater)
    }

    override fun initViewAndData(
        saveInstanceState: Bundle?,
        binding: DoneDialogBinding
    ) {
        setSizeWrapForDialog()

        val title = arguments?.getString(ARG_TITLE)
        binding.textTitle.text = title
    }

    override fun onStart() {
        super.onStart()
        Handler(Looper.getMainLooper()).postDelayed({
            if (isAdded) dismissAllowingStateLoss()
        }, 2500)
    }

    companion object {
        const val TAG = "DoneDialog"
        private const val ARG_TITLE = "ARG_TITLE"

        fun newInstance(title: String): DoneDialog {
            return DoneDialog().apply {
                arguments = Bundle().apply {
                    putString(ARG_TITLE, title)
                }
            }
        }
    }
}

