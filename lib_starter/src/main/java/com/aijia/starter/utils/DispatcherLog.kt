package com.aijia.starter.utils

import com.aijia.framework.helper.AppHelper
import com.aijia.framework.log.LogUtil


object DispatcherLog {
    var isDebug = AppHelper.isDebug()

    @JvmStatic
    fun i(msg: String?) {
        if (msg == null) {
            return
        }
        LogUtil.i(msg, tag = "StartTask")
    }
}