package ru.maximmakarov.extensions

import java.util.*


fun Calendar.cutMs(): Calendar {
    set(Calendar.MILLISECOND, 0)
    return this
}

fun Long.sToMs(): Long = this * 1000L

fun Long.msToS(): Long = this / 1000L