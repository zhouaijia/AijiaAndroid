package com.aijia.log

import android.util.Log

/**
 * Created by Aijia on 2024/3/28.
 */
object LogImplForLogcat : LogImpl {

    override fun getName(): String {
        return "LogcatForAijia"
    }

    override fun i(tag: String?, msg: String?) {
        Log.i(tag, msg ?: "")
    }

    override fun d(tag: String?, msg: String?) {
        Log.d(tag, msg?: "")
    }

    override fun w(tag: String?, msg: String?) {
        Log.w(tag, msg?: "")
    }

    override fun e(tag: String?, msg: String?) {
        Log.e(tag, msg?: "")
    }

    override fun info(tag: String?, msg: String?) {
        Log.i(tag, msg?: "")
    }
}