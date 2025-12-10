package com.android.seclearning.common.utils

import android.annotation.SuppressLint
import android.app.ActivityManager
import android.content.Context
import android.os.Build
import android.provider.Settings
import android.text.InputType
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.widget.AppCompatEditText
import java.util.Date
import java.util.Locale
import java.util.TimeZone
import java.util.zip.CRC32
import kotlin.math.abs
import kotlin.text.contains
import kotlin.text.format
import kotlin.text.isNullOrEmpty
import kotlin.text.lowercase
import kotlin.text.startsWith
import kotlin.text.substring
import kotlin.text.toByteArray

object ContextExt {
    private fun getMemory(): ActivityManager.MemoryInfo {
        val memInfo = ActivityManager.MemoryInfo()
        activityManager?.getMemoryInfo(memInfo)
        return memInfo
    }

    val memoryByGB: Double get() = getMemory().totalMem / 1073741824.0

    fun getDeviceName(): String {
        val manufacturer = Build.MANUFACTURER
        val model = Build.MODEL
        return if (model.lowercase(Locale.getDefault())
                .startsWith(manufacturer.lowercase(Locale.getDefault()))
        ) {
            capitalize(model)
        } else {
            capitalize(manufacturer) + " " + model
        }
    }

    fun getDeviceModel(): String {
        return Build.MODEL
    }

    fun getTimeZoneDefault(): String {
        val timeZone = TimeZone.getDefault()
        var offset = timeZone.rawOffset
        if (timeZone.inDaylightTime(Date())) {
            offset += timeZone.dstSavings
        }
        val hours = offset / (1000 * 60 * 60)
        val minutes = abs((offset / (1000 * 60)) % 60)
        return String.format("GMT%+d:%02d", hours, minutes)
    }

    @SuppressLint("HardwareIds")
    fun Context.getMobileId(): String {
        return Settings.Secure.getString(contentResolver, Settings.Secure.ANDROID_ID)
    }

    fun getDeviceId(deviceModel: String): Long {
        val crc32 = CRC32()
        crc32.reset()
        crc32.update(deviceModel.lowercase().toByteArray())
        return crc32.value
    }

    private fun transformAge(range: String): String {
        return if (range.contains("0-18")) "18"
        else if (range.contains("18-24")) "1824"
        else if (range.contains("25-34")) "2534"
        else if (range.contains("35-44")) "3544"
        else if (range.contains("45-100")) "45"
        else "0"
    }

    private fun capitalize(s: String?): String {
        if (s.isNullOrEmpty()) {
            return ""
        }
        val first = s[0]
        return if (Character.isUpperCase(first)) {
            s
        } else {
            Character.toUpperCase(first).toString() + s.substring(1)
        }
    }

    fun getAgeRangeForBackEnd(): String {
        // TODO: update code here
        return ""
    }

    fun hideKeyboard(view: View) {
        val imm =
            view.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }


    fun togglePasswordCommon(editText: AppCompatEditText) {
        val show = editText.isSelected

        editText.inputType = if (show) {
            InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
        } else {
            InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
        }

        editText.setSelection(editText.text?.length ?: 0)
    }

}