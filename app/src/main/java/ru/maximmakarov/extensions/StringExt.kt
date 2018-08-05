package ru.maximmakarov.extensions


fun String.isEmail() = android.util.Patterns.EMAIL_ADDRESS.matcher(this).matches()

fun String.isNumeric(): Boolean {
    val p = "^[0-9]+$".toRegex()
    return matches(p)
}