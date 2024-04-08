package com.aijia.android.task

import android.app.Application
import com.aijia.android.BuildConfig
import com.aijia.framework.helper.AppHelper
import com.aijia.framework.log.LogUtil
import com.aijia.framework.manager.AppManager
import com.aijia.starter.task.Task
import com.aijia.starter.utils.DispatcherExecutor
import com.alibaba.android.arouter.launcher.ARouter
import com.scwang.smart.refresh.footer.ClassicsFooter
import com.scwang.smart.refresh.header.ClassicsHeader
import com.scwang.smart.refresh.layout.SmartRefreshLayout
import com.tencent.mmkv.MMKV
import java.util.concurrent.ExecutorService


/**
 * 初始化全局帮助类
 */
class InitAppHelperTask(private val application: Application): Task() {
    //异步线程执行的Task在被调用await的时候等待
    override fun needWait(): Boolean {
        return true
    }

    override fun run() {
        AppHelper.init(application, BuildConfig.DEBUG)
    }
}

/**
 * 初始化MMKV
 */
class InitMMKVTask: Task() {
    override fun needWait(): Boolean {
        return true
    }

    //依赖某些任务，在某些任务完成后才能执行
    override fun dependsOn(): List<Class<out Task?>?> {
        val tasks = mutableListOf<Class<out Task?>>()
        tasks.add(InitAppHelperTask::class.java)
        return tasks
    }

    override fun runOn(): ExecutorService? {
        return DispatcherExecutor.IOExecutor
    }

    override fun run() {
        val rootDir: String = MMKV.initialize(AppHelper.getApplication())
        LogUtil.d("mmkv root: $rootDir", tag = "MMKV")
    }
}

class InitAppManagerTask: Task() {
    override fun needWait(): Boolean {
        return true
    }

    override fun dependsOn(): List<Class<out Task>> {
        val tasks = mutableListOf<Class<out Task?>>()
        tasks.add(InitAppHelperTask::class.java)
        return tasks
    }

    override fun run() {
        AppManager.init(AppHelper.getApplication())
    }
}

/**
 * 初始化ARouter
 */
class InitARouterTask() : Task() {
    //异步线程执行的Task在被调用await的时候等待
    override fun needWait(): Boolean {
        return true
    }

    //依赖某些任务，在某些任务完成后才能执行
    override fun dependsOn(): MutableList<Class<out Task>> {
        val tasks = mutableListOf<Class<out Task?>>()
        tasks.add(InitAppHelperTask::class.java)
        return tasks
    }

    //执行任务，任务真正的执行逻辑
    override fun run() {
        // 这两行必须写在init之前，否则这些配置在init过程中将无效
        //ARouter.openLog()
        //ARouter.openDebug()
        ARouter.init(AppHelper.getApplication())
    }
}


/**
 * 全局初始化SmartRefreshLayout
 */
class InitRefreshLayoutTask() : Task() {
    //异步线程执行的Task在被调用await的时候等待
    override fun needWait(): Boolean {
        return true
    }

    override fun run() {
        //设置全局的Header构建器
        SmartRefreshLayout.setDefaultRefreshHeaderCreator { context, layout ->
            layout.setPrimaryColorsId(android.R.color.white)
//            CustomRefreshHeader(context)
            ClassicsHeader(context)
        }
        //设置全局的Footer构建器
        SmartRefreshLayout.setDefaultRefreshFooterCreator { context, layout ->
            //指定为经典Footer，默认是 BallPulseFooter
            ClassicsFooter(context)
        }
    }
}