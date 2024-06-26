package com.aijia.common.service

import android.content.Context
import com.alibaba.android.arouter.facade.template.IProvider


/**
 * @author Aijia
 * @date   2024/4/1
 * @desc   主页模块相关接口
 * 提供主页模块对外能力，其他模块只需要按需添加，需要在Main模块实现
 */
interface IMainService: IProvider {
    /**
     * 跳转主页
     * @param context
     * @param index tab位置
     */
    fun toMain(context: Context, index: Int)
}