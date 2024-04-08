package com.aijia.framework.helper

import android.app.Application

/**
 * @author Aijia
 * @date   2024/4/7
 * @desc   提供应用环境
 */
object AppHelper {
    private lateinit var app: Application
    private var isDebug = false

    fun init(application: Application, isDebug: Boolean) {
        this.app = application
        this.isDebug = isDebug
    }

    /**
     * 获取全局应用
     */
    fun getApplication() = app

    /**
     * 是否为debug环境
     */
    fun isDebug() = isDebug
}