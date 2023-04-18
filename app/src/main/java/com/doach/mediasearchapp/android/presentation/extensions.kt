package com.doach.mediasearchapp.android.presentation

import android.app.Activity
import android.content.Context
import android.net.Uri
import android.widget.Toast
import androidx.browser.customtabs.CustomTabsIntent
import androidx.fragment.app.Fragment
import com.doach.mediasearchapp.android.App
import com.doach.mediasearchapp.android.di.AppContainer
import java.text.SimpleDateFormat
import java.util.*


fun Activity.getContainer(): AppContainer {
    return (application as App).appContainer
}
fun Fragment.getContainer(): AppContainer {
    return requireActivity().getContainer()
}
fun Float.toPixel(context: Context): Int = (context.resources.displayMetrics.density * this).toInt()

private var toast: Toast? = null
fun Context.showToast(msg: String) {
    toast?.cancel()
    toast = Toast.makeText(this, msg, Toast.LENGTH_SHORT)
    toast?.show()
}
fun Fragment.showToast(msg: String) {
    toast?.cancel()
    toast = Toast.makeText(requireContext(), msg, Toast.LENGTH_SHORT)
    toast?.show()
}

fun Long.toFormattedTime(pattern: String): String {
    val sdf = SimpleDateFormat(pattern, Locale.KOREA)
    return sdf.format(Date(this))
}

fun Long.formatDuration(): String {
    val hours = this / 3600
    val minutes = (this % 3600) / 60
    val seconds = this % 60

    return String.format("%02d:%02d:%02d", hours, minutes, seconds)
}

fun Context.openCustomTab(url: String) {
    val builder = CustomTabsIntent.Builder()
    val customTabsIntent = builder.build()
    customTabsIntent.launchUrl(this, Uri.parse(url))
}