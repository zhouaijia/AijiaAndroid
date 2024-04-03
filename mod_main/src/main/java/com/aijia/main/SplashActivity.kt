package com.aijia.main

import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import com.aijia.common.provider.MainServiceProvider
import com.aijia.framework.base.BaseDataBindActivity
import com.aijia.framework.ext.countDownCoroutines
import com.aijia.framework.ext.onClick
import com.aijia.framework.utils.StatusBarSettingHelper
import com.aijia.main.databinding.ActivitySplashBinding


class SplashActivity : BaseDataBindActivity<ActivitySplashBinding>() {

    override fun initView(savedInstanceState: Bundle?) {
        StatusBarSettingHelper.setStatusBarTranslucent(this)

        mBinding.tvSkip.onClick {
            MainServiceProvider.toMain(this)
            finish()
        }
        //倒计时
        countDownCoroutines(2, lifecycleScope, onTick = {
            mBinding.tvSkip.text = getString(R.string.splash_time, it.plus(1).toString())
        }) {
            if (!this@SplashActivity.isFinishing) {
                MainServiceProvider.toMain(this)
                finish()
            }
        }
    }
}