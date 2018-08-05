package ru.maximmakarov.extensions

import android.annotation.SuppressLint
import android.app.Activity
import android.app.ActivityManager
import android.app.KeyguardManager
import android.app.NotificationManager
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.net.ConnectivityManager
import android.net.Uri
import android.net.wifi.WifiManager
import android.os.Build
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Toast
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.annotation.LayoutRes
import androidx.annotation.StringRes
import androidx.appcompat.app.AlertDialog
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.google.android.material.snackbar.Snackbar

fun Fragment.toast(@StringRes messageResId: Int, duration: Int = Toast.LENGTH_SHORT) {
    activity?.toast(messageResId, duration)
}

fun Fragment.toast(message: String, duration: Int = Toast.LENGTH_SHORT) {
    activity?.toast(message, duration)
}


fun Context.toast(@StringRes messageResId: Int, duration: Int = Toast.LENGTH_SHORT) {
    toast(getString(messageResId), duration)
}

fun Context.toast(message: String, duration: Int = Toast.LENGTH_SHORT) {
    if ((this is Activity && !this.isFinishing) || this !is Activity) Toast.makeText(this, message, duration).show()
}

fun Fragment.snackBar(@StringRes messageResId: Int, duration: Int = Snackbar.LENGTH_SHORT) {
    activity?.snackBar(messageResId, duration = duration)
}

fun FragmentActivity.snackBar(@StringRes messageResId: Int, view: View = window.decorView.rootView, duration: Int = Snackbar.LENGTH_SHORT) {
    Snackbar.make(view, getString(messageResId), duration).show()
}

fun Context.alert(title: Int, message: Int, posTitle: Int, positiveAction: (dialog: DialogInterface) -> Unit,
                  negTitle: Int? = null, negativeAction: ((dialog: DialogInterface) -> Unit)? = null,
                  cancelable: Boolean = true) {
    alert(getString(title), getString(message), getString(posTitle), positiveAction,
            negTitle?.let { getString(negTitle) }, negativeAction, cancelable)
}

fun Context.alert(title: String, message: String, posTitle: String, positiveAction: (dialog: DialogInterface) -> Unit,
                  negTitle: String? = null, negativeAction: ((dialog: DialogInterface) -> Unit)? = null,
                  cancelable: Boolean = true) {
    AlertDialog.Builder(this)
            .setPositiveButton(posTitle, { dialog, _ -> positiveAction(dialog) })
            .setTitle(title)
            .setMessage(message).apply {
                if (negTitle != null)
                    setNegativeButton(negTitle, { dialog, _ -> negativeAction!!(dialog) })
                setCancelable(cancelable)
                show()
            }
}

fun <T> Context.listDialog(title: String, list: List<T>, onItemClickAction: (T) -> Unit) {
    val adapter = ArrayAdapter<T>(this, android.R.layout.select_dialog_item, list)
    AlertDialog.Builder(this)
            .setTitle(title)
            .setAdapter(adapter, { _, pos -> onItemClickAction(adapter.getItem(pos)) })
            .show()
}

@SuppressLint("InflateParams")
fun Context.inputDialog(title: Int, hint: Int,
                        posTitle: Int, posAction: (dialog: DialogInterface, text: String) -> Unit,
                        negTitle: Int, negAction: (dialog: DialogInterface) -> Unit,
                        text: String = "", @LayoutRes editResId: Int) {

    val view = LayoutInflater.from(this).inflate(editResId, null, false)
    val edit = view.findViewById<EditText>(R.id.text).apply {
        setHint(hint)
        setText(text)
        setSelection(text.length)
    }

    AlertDialog.Builder(this)
            .setTitle(title)
            .setView(view)
            .setPositiveButton(posTitle, { dialog, _ -> posAction(dialog, edit.text.toString()) })
            .setNegativeButton(negTitle, { dialog, _ -> negAction(dialog) })
            .setCancelable(false)
            .show()
}

fun Context.notification(notifId: Int, @StringRes title: Int, @StringRes message: Int, @DrawableRes icon: Int, autocancel: Boolean = true) {
    notification(notifId, getString(title), getString(message), icon, autocancel)
}

fun Context.notification(notifId: Int, title: String, message: String, @DrawableRes icon: Int, autocancel: Boolean = true) {
    val notification = NotificationCompat.Builder(this, "myhome")
            .setSmallIcon(icon)
            .setContentTitle(title)
            .setContentText(message)
            .setAutoCancel(autocancel)
            .build()

    val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager?
    notificationManager?.notify(notifId, notification)
}

//fun Context.pxToDp(px: Float) = px / this.resources.displayMetrics.density
//fun Context.dpToPx(dp: Float) = dp * this.resources.displayMetrics.density

fun Context.dpToPx(dp: Int) = Math.round(dp * (resources.displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT))

fun Context.getColorCompat(@ColorRes colorResId: Int) = ContextCompat.getColor(this, colorResId)

fun Context.getDrawableCompat(@DrawableRes drawableResId: Int) = ContextCompat.getDrawable(this, drawableResId)

fun Context.launchService(cls: Class<*>, isForeground: Boolean = true): Intent {
    val i = Intent(this, cls)
    if (isForeground && Build.VERSION.SDK_INT >= 26) startForegroundService(i)
    else startService(i)
    return i
}

fun Context.isNetworkAvailable(): Boolean {
    val cm = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val network = cm.activeNetworkInfo
    return network != null && network.isConnected
}

fun Context.isWifi(): Boolean {
    val cm = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val network = cm.activeNetworkInfo
    return network?.type == ConnectivityManager.TYPE_WIFI
}

fun Context.getCurrentSsid(): String? {
    if (isWifi()) {
        val wifiManager = applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager?
        val ssid = wifiManager?.connectionInfo?.ssid
        if (ssid?.isNotEmpty() == true && ssid != "<unknown ssid>") {
            return ssid.trim { it == '\'' || it == '\"' }
        }
    }
    return null
}

fun Context.isAppInForeground(): Boolean {
    if (isScreenLocked()) return false

    //any of running process
    val activityManager = getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager?
    val appProcesses = activityManager?.runningAppProcesses ?: return false
    return appProcesses.any { it.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND && it.processName == BuildConfig.APPLICATION_ID }
}

fun Context.isScreenLocked(): Boolean {
    return (getSystemService(Context.KEYGUARD_SERVICE) as KeyguardManager?)?.inKeyguardRestrictedInputMode()
            ?: true
}

fun Context.showAppInMarket(packageName: String) {
    try {
        startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=$packageName")))
    } catch (e: android.content.ActivityNotFoundException) {
        startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=$packageName")))
    }
}