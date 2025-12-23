package com.android.seclearning.common.utils

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class CopyPasteManager@Inject constructor(@ApplicationContext val context: Context) {

    fun copyToClipboard(label: String, text: String) {
        val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clip = ClipData.newPlainText(label, text)
        clipboard.setPrimaryClip(clip)
    }

    fun pasteFromClipboard(): String? {
        val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
//        if (!clipboard.hasPrimaryClip()) {
//            return null
//        } else if (clipboard.primaryClipDescription?.hasMimeType(android.content.ClipDescription.MIMETYPE_TEXT_PLAIN) != true) {
//            return null
//        }
        val item = clipboard.primaryClip?.getItemAt(0)
        return item?.text?.toString()
    }

    fun canPaste(): Boolean {
        val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        return clipboard.hasPrimaryClip() /*&& clipboard.primaryClipDescription?.hasMimeType(android.content.ClipDescription.MIMETYPE_TEXT_PLAIN) == true*/
    }

}