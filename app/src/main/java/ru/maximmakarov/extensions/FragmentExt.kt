package ru.maximmakarov.extensions

import androidx.annotation.IdRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager


fun FragmentActivity.replaceFragment(fragment: Fragment, @IdRes containerId: Int = R.id.container,
                                     tag: String = fragment::class.java.name, backStack: Boolean = false, replaceFragments: Boolean = true) {
    supportFragmentManager.beginTransaction().apply {
        if (replaceFragments) replace(containerId, fragment, tag)
        else add(containerId, fragment, tag)

        if (backStack) addToBackStack(null)
    }.commit()
}

fun FragmentActivity.clearBackstack() {
    supportFragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)
}

fun FragmentActivity.getFragment(@IdRes containerId: Int = R.id.container): Fragment? = supportFragmentManager.findFragmentById(containerId)

fun <K : Fragment> FragmentActivity.getFragment(clazz: Class<K>): Fragment? = supportFragmentManager.findFragmentByTag(clazz.name)

fun <K : Fragment> FragmentActivity.removeFragment(clazz: Class<K>) {
    val fragment = getFragment(clazz)
    if (fragment != null) {
        supportFragmentManager.beginTransaction().remove(fragment).commit()
    }
}