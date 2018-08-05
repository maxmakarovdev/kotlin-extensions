package ru.maximmakarov.extensions

import android.text.SpannableString
import android.text.Spanned
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import androidx.annotation.StringRes

fun View.onClick(action: () -> Unit) {
    setOnClickListener { action() }
}

fun View.onLongCLick(action: () -> Unit) {
    setOnLongClickListener { action(); true }
}

fun View.clearOnClick() {
    setOnClickListener(null)
}

fun View.visibleOrInvisible(isVisible: Boolean) {
    visibility = if (isVisible) View.VISIBLE else View.INVISIBLE
}

fun View.visibleOrGone(isVisible: Boolean) {
    visibility = if (isVisible) View.VISIBLE else View.GONE
}

fun View.visible() {
    visibility = View.VISIBLE
}

fun View.invisible() {
    visibility = View.INVISIBLE
}

fun View.gone() {
    visibility = View.GONE
}

fun Array<View>.visible() = forEach { it.visible() }
fun Array<View>.invisible() = forEach { it.invisible() }
fun Array<View>.gone() = forEach { it.gone() }
fun Array<View>.visibleOrGone(isVisible: Boolean) = forEach { it.visibleOrGone(isVisible) }
fun Array<View>.visibleOrInvisible(isVisible: Boolean) = forEach { it.visibleOrInvisible(isVisible) }


fun TextView.handleEnterKey(action: () -> Unit) {
    setOnEditorActionListener { _, actionId, event ->
        var handled = false
        if (event == null) {
            if (actionId == EditorInfo.IME_ACTION_DONE || actionId == EditorInfo.IME_ACTION_NEXT) {
                handled = true
                action()
            }
        } else if (actionId == EditorInfo.IME_NULL) {
            handled = true
            if (event.action == KeyEvent.ACTION_DOWN)
                action()
        }
        handled
    }
}

fun TextView.setClickableTail(@StringRes strRes: Int, args: Array<String>, action: () -> Unit) {
    val ss = when (args.size) {
        1 -> SpannableString(context.getString(strRes, args[0]))
        2 -> SpannableString(context.getString(strRes, args[0], args[1]))
        else -> SpannableString("")
    }
    val clickableSpan = object : ClickableSpan() {
        override fun onClick(textView: View) = action()
    }
    ss.setSpan(clickableSpan, ss.length - args[args.size - 1].length, ss.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
    text = ss
    movementMethod = LinkMovementMethod.getInstance()
}
