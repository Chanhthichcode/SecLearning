package com.android.seclearning.ui.dialog

import android.os.Bundle
import android.view.LayoutInflater
import com.android.seclearning.databinding.DoneDialogBinding
import com.android.seclearning.ui.common.base.BaseDialog

class DoneDialog() : BaseDialog<DoneDialogBinding>() {

    override fun makeBinding(inflater: LayoutInflater): DoneDialogBinding {
        return DoneDialogBinding.inflate(layoutInflater)
    }

    override fun initViewAndData(
        saveInstanceState: Bundle?,
        binding: DoneDialogBinding
    ) {
        setSizeWrapForDialog()
    }

    companion object{
        const val TAG = "DoneDialog"
    }
}
