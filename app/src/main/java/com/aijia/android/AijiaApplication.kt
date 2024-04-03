package com.aijia.android

import android.app.Application
import com.alibaba.android.arouter.launcher.ARouter


class AijiaApplication: Application() {

    override fun onCreate() {
        super.onCreate()

        // 这两行必须写在init之前，否则这些配置在init过程中将无效
        ARouter.openLog()
        ARouter.openDebug()
        ARouter.init(this)
    }
}