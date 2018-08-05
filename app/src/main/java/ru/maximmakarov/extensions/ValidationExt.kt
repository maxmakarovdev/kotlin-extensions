package ru.maximmakarov.extensions

import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.annotation.StringRes


fun ViewGroup.validateTextsOnEmpty(@StringRes messageResId: Int): Boolean =
        validateTextsOnEmpty(context.getString(messageResId))

fun ViewGroup.validateTextsOnEmpty(message: String): Boolean {
    var isValidated = true
    (0 until childCount)
            .asSequence()
            .map { getChildAt(it) }
            .filter { it.visibility != View.GONE && it.isEnabled }
            .forEach { isValidated = isValidated and ((it as? ViewGroup)?.validateTextsOnEmpty(message) ?: it.validateTextOnEmpty(message)) }
    return isValidated
}

fun View.validateTextOnEmpty(@StringRes messageResId: Int): Boolean =
        validateTextOnEmpty(context.getString(messageResId))

fun View.validateTextOnEmpty(message: String): Boolean {
    var isValidated = true
    if (this is EditText) {
        text.toString().apply {
            error = if (isNotEmpty()) null else message
            isValidated = isNotEmpty()
        }
    }
    return isValidated
}