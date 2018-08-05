package ru.maximmakarov.extensions

import android.app.Activity
import android.content.Context
import android.view.inputmethod.InputMethodManager

fun Activity.showKeyboard() {
    val view = currentFocus
    view?.let {
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        return imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0)
    }
}

fun Activity.hideKeyboard(): Boolean {
    val view = currentFocus
    view?.let {
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        return imm.hideSoftInputFromWindow(view.windowToken, 0)
    }
    return false
}