package com.aijia.starter

import com.aijia.framework.log.LogUtil
import java.util.concurrent.atomic.AtomicInteger

/**
 * 任务开始
 */
object TaskStart {
    @Volatile
    private var mCurrentSituation = ""
    private val mBeans: MutableList<TaskStartBean> = ArrayList()
    private var mTaskDoneCount = AtomicInteger()

    /**
     * 是否开启统计
     */
    private const val sOpenLaunchStart = false
    var currentSituation: String
        get() = mCurrentSituation
        set(currentSituation) {
            if (!sOpenLaunchStart) {
                return
            }
            LogUtil.i("currentSituation   $currentSituation")
            mCurrentSituation = currentSituation
            setLaunchStart()
        }

    fun markTaskDone() {
        mTaskDoneCount.getAndIncrement()
    }

    fun setLaunchStart() {
        val bean = TaskStartBean()
        bean.situation = mCurrentSituation
        bean.count = mTaskDoneCount.get()
        mBeans.add(bean)
        mTaskDoneCount = AtomicInteger(0)
    }
}