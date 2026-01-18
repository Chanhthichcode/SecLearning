package com.android.seclearning.ui.main.home.lab.dialog

import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import com.android.seclearning.common.utils.setSafeOnClickListener
import com.android.seclearning.databinding.DialogCreateNewLabBinding
import com.android.seclearning.ui.common.base.BaseDialog

class DialogCreateNewLab : BaseDialog<DialogCreateNewLabBinding>() {

    private var onConfirmListener: ((String) -> Unit)? = null

    fun setOnConfirmListener(listener: (String) -> Unit) {
        onConfirmListener = listener
    }

    override fun makeBinding(inflater: LayoutInflater): DialogCreateNewLabBinding =
        DialogCreateNewLabBinding.inflate(inflater)

    override fun getGravityForDialog(): Int = Gravity.CENTER

    override fun initViewAndData(
        saveInstanceState: Bundle?,
        binding: DialogCreateNewLabBinding
    ) {
        isCancelable = true
        setupClickListeners(binding)
    }

    private fun setupClickListeners(binding: DialogCreateNewLabBinding) = with(binding) {

        btnSave.setSafeOnClickListener {
            val link = tvLink.text.toString().trim()
            if (link.isNotEmpty()) {
                onConfirmListener?.invoke(link)
                dismiss()
            }
        }

        btnBack.setSafeOnClickListener {
            dismiss()
        }

        root.setSafeOnClickListener {
            dismiss()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        onConfirmListener = null
    }

    companion object {
        const val TAG = "DialogCreateNewLab"
    }
}
