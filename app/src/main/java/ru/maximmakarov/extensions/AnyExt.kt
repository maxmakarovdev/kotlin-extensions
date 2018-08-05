package ru.maximmakarov.extensions

fun <T> Iterable<T>.containsAnyOf(args: Iterable<T>): Boolean = any { args.contains(it) }

fun <T> List<T>.equalsByContent(args: List<T>): Boolean = containsAll(args) && args.containsAll(this)
