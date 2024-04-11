package com.aijia.framework.base

import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.viewbinding.ViewBinding
import java.lang.reflect.ParameterizedType

/**
 * @author Aijia
 * @date   2024/4/8
 * @desc   DataBinding和ViewModel基类，用于MVVM架构
 */
abstract class BaseMvvmFragment<VB: ViewBinding, VM: ViewModel>: BaseDataBindFragment<VB>() {
    lateinit var mViewModel: VM

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initViewModel()
        super.onViewCreated(view, savedInstanceState)
    }

    private fun initViewModel() {
        val argument = (this.javaClass.genericSuperclass as ParameterizedType).actualTypeArguments
        mViewModel = ViewModelProvider(this)[argument[1] as Class<VM>]
    }

}