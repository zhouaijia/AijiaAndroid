package com.aijia.common.provider

import android.content.Context
import com.aijia.common.constant.MAIN_SERVICE_HOME
import com.aijia.common.service.IMainService
import com.alibaba.android.arouter.facade.annotation.Autowired
import com.alibaba.android.arouter.launcher.ARouter

/**
 * @author Aijia
 * @date   2024/4/1
 * @desc   MainService提供类，对外提供相关能力
 * 任意模块就能通过MainServiceProvider使用对外暴露的能力
 */
object MainServiceProvider {

    @Autowired(name = MAIN_SERVICE_HOME)
    lateinit var mainService: IMainService// = ARouter.getInstance().build(MAIN_SERVICE_HOME).navigation() as? IMainService

    init {
        ARouter.getInstance().inject(this)
    }

    /**
     * 跳转主页
     * @param context
     * @param index tab位置
     */
    fun toMain(context: Context, index: Int = 0) {
        mainService.toMain(context, index)
    }
}