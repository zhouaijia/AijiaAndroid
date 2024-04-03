package com.aijia.main.service

import android.content.Context
import com.aijia.common.constant.MAIN_SERVICE_HOME
import com.aijia.common.service.IMainService
import com.aijia.main.MainActivity
import com.alibaba.android.arouter.facade.annotation.Route

@Route(path = MAIN_SERVICE_HOME)
class MainService: IMainService {
    /**
     * 跳转主页
     * @param context
     * @param index tab位置
     */
    override fun toMain(context: Context, index: Int) {
        MainActivity.start(context, index)
    }

    override fun init(context: Context?) {
    }
}