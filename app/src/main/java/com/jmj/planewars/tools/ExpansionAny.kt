package com.jmj.planewars.tools

import android.util.Log
import com.jmj.planewars.App
import com.jmj.planewars.BuildConfig


/**
 * 日志打印
 */
fun Any.myLog(msg: String, logType: String = "e") {
    if (!BuildConfig.DEBUG) {
        return
    }
    val stacks = Throwable().stackTrace
    val className = this::class.java.simpleName
    val methodName = stacks[1].methodName
    val lineNumber = stacks[1].lineNumber

    var logString = "MyLog:($className::$methodName::$lineNumber)"

    when (logType) {
        "i" -> {
            Log.i(logString, msg)
        }
        "d" -> {
            Log.d(logString, msg)
        }
        "v" -> {
            Log.v(logString, msg)
        }
        "w" -> {
            Log.w(logString, msg)
        }
        "e" -> {
            Log.e(logString, msg)
        }
    }

}

fun Any.myLog(vararg msg: Any) {
    if (!BuildConfig.DEBUG) {
        return
    }
    var s = ""
    msg.forEach {
        s += "$it,"
    }
    myLog(s)
}


fun Any.dp2px(dp: Int): Int {
    var scale = App.getInstance().resources.displayMetrics.density
    return return ((dp * scale + 0.5f).toInt())
}

fun Any.px2dp(px: Int): Int {
    var scale = App.getInstance().resources.displayMetrics.density
    return return ((px / scale + 0.5f).toInt())
}




