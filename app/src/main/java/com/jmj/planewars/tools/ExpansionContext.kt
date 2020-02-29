package com.jmj.planewars.tools

import android.content.Context
import android.widget.Toast
import androidx.annotation.StringRes


var SHARE_FILE_NAME=""

fun Context.showToast(msg: String) {
    if (msg.isNotEmpty()) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
    }
}

fun Context.showToast(@StringRes msg: Int) {
    Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
}

/**
 * SharedPreferences
 */
fun Context.putStringToShared(key: String, value: String?) {
    getSharedPreferences(SHARE_FILE_NAME, Context.MODE_PRIVATE)
        .edit()
        .putString(key, value)
        .apply()
}

fun Context.getStringByShared(key: String): String? {
    return getSharedPreferences(SHARE_FILE_NAME, Context.MODE_PRIVATE)
        .getString(key, "")
}

fun Context.putIntToShared(key: String, value: Int) {
    getSharedPreferences(SHARE_FILE_NAME, Context.MODE_PRIVATE)
        .edit()
        .putInt(key, value)
        .apply()
}

fun Context.getIntByShared(key: String): Int {
    return getSharedPreferences(SHARE_FILE_NAME, Context.MODE_PRIVATE)
        .getInt(key, -1)
}

fun Context.putBooleanToShared(key: String, value: Boolean) {
    getSharedPreferences(SHARE_FILE_NAME, Context.MODE_PRIVATE)
        .edit()
        .putBoolean(key, value)
        .apply()
}

fun Context.getBooleanByShared(key: String): Boolean {
    return getSharedPreferences(SHARE_FILE_NAME, Context.MODE_PRIVATE)
        .getBoolean(key, false)
}



fun Context.dp2px(dp: Int): Int {
    var scale = this.resources.displayMetrics.density
    return return ((dp * scale + 0.5f).toInt())
}

fun Context.px2dp(px: Int): Int {
    var scale = this.resources.displayMetrics.density
    return return ((px / scale + 0.5f).toInt())
}



