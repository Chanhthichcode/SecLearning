package com.android.seclearning.ui.dialog
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import com.android.seclearning.common.utils.setSafeOnClickListener
import com.android.seclearning.databinding.NoInternetDialogBinding
import com.android.seclearning.ui.common.base.BaseDialog

class NoInternetDialog : BaseDialog<NoInternetDialogBinding>() {

    private var onClickBack: (() -> Unit)? = null
    private var onClickViewSettings: (() -> Unit)? = null

    fun setOnClickBack(onClickBack: () -> Unit) {
        this.onClickBack = onClickBack
    }

    fun setOnClickViewSettings(onClickViewSettings: () -> Unit) {
        this.onClickViewSettings = onClickViewSettings
    }

    override fun makeBinding(inflater: LayoutInflater): NoInternetDialogBinding {
        return NoInternetDialogBinding.inflate(inflater)
    }

    override fun getGravityForDialog(): Int {
        return Gravity.BOTTOM
    }

    override fun initViewAndData(
        saveInstanceState: Bundle?,
        binding: NoInternetDialogBinding
    ) {
        this.isCancelable = true
        binding.buttonBackToMyFile.setSafeOnClickListener {
            onClickBack?.invoke()
            dismissDialog(TAG)
        }

        binding.buttonNetworkSetting.setSafeOnClickListener {
            onClickViewSettings?.invoke()
            dismissDialog(TAG)
        }

        binding.ivCloseNoInternet.setSafeOnClickListener {
            dismissDialog(TAG)
        }
    }

    companion object {
        const val TAG = "NoInternetDialog"
    }
}