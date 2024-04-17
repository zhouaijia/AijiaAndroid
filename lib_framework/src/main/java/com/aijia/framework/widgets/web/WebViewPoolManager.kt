package com.aijia.framework.widgets.web

import android.content.Context
import android.content.MutableContextWrapper
import android.os.Looper
import android.view.ViewGroup


/**
 * @author Aijia
 * @date 2024/4/14
 * @desc WebView缓存类
 */
object WebViewPoolManager {

    private val webViewCache: MutableList<MyWebView> = ArrayList(1)

    private fun create(context: Context): MyWebView {
        return MyWebView(context)
    }

    /**
     * 初始化
     */
    @JvmStatic
    fun prepare(context: Context) {
        if (webViewCache.isEmpty()) {
            Looper.myQueue().addIdleHandler {
                webViewCache.add(create(MutableContextWrapper(context)))
                false
            }
        }
    }

    /**
     * 获取WebView
     */
    @JvmStatic
    fun obtain(context: Context): MyWebView {
        if (webViewCache.isEmpty()) {
            webViewCache.add(create(MutableContextWrapper(context)))
        }
        val webView = webViewCache.removeFirst()
        val contextWrapper = webView.context as MutableContextWrapper
        contextWrapper.baseContext = context
        webView.clearHistory()
        webView.resumeTimers()
        return webView
    }

    /**
     * 回收资源
     */
    @JvmStatic
    fun recycle(webView: MyWebView) {
        try {
            webView.stopLoading()
            webView.loadDataWithBaseURL(null, "", "text/html", "utf-8", null)
            webView.clearHistory()
            webView.pauseTimers()
            webView.clearFormData()
            webView.removeJavascriptInterface("webkit")

            // 重置并回收当前的上下文对象，根据池容量判断是否销毁，也可以置换为ApplicationContext
            val contextWrapper = webView.context as MutableContextWrapper
            contextWrapper.baseContext = webView.context.applicationContext

            val parent = webView.parent
            if (parent != null) {
                (parent as ViewGroup).removeView(webView)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            if (!webViewCache.contains(webView)) {
                webViewCache.add(webView)
            }
        }
    }

    /**
     * 销毁资源
     */
    @JvmStatic
    fun destroy() {
        try {
            webViewCache.forEach {
                it.removeAllViews()
                it.destroy()
                webViewCache.remove(it)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

}