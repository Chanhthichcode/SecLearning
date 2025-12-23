package com.android.seclearning.common.utils

import android.graphics.Color
import android.graphics.LinearGradient
import android.graphics.Shader
import android.graphics.Typeface
import android.text.SpannableString
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan
import android.text.style.UnderlineSpan
import android.widget.TextView
import kotlin.collections.map
import kotlin.collections.toIntArray
import androidx.core.graphics.toColorInt

fun TextView.setColorGradient(hexColors: List<String>) {
    val width = paint.measureText(text.toString())
    val textShader = LinearGradient(
        0f, 0f, width, height.toFloat(),
        hexColors.map { it.toColorInt() }.toIntArray(),
        null, Shader.TileMode.CLAMP
    )
    paint.shader = textShader
    invalidate()
    requestLayout()
}

fun TextView.setColor(hexColor: String) {
    paint.shader = null
    invalidate()
    requestLayout()
    setTextColor(hexColor.toColorInt())
}


fun boldLabel(label: String, value: String?): SpannableString {
    val safeValue = value.orEmpty()
    val text = "$label: $safeValue"
    return SpannableString(text).apply {
        setSpan(
            StyleSpan(Typeface.BOLD),
            0,
            label.length + 1,
            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )
    }
}

fun boldLabelWithUnderlineUrl(
    label: String,
    url: String?
): SpannableString {

    val safeUrl = url.orEmpty()
    val text = "$label: $safeUrl"
    val start = label.length + 2
    val end = text.length

    return SpannableString(text).apply {

        // Bold label
        setSpan(
            StyleSpan(Typeface.BOLD),
            0,
            label.length + 1,
            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )

        // Underline URL
        setSpan(
            UnderlineSpan(),
            start,
            end,
            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )

        // URL màu xanh dương
        setSpan(
            ForegroundColorSpan(Color.BLUE),
            start,
            end,
            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )
    }
}