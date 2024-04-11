package com.aijia.framework.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewbinding.ViewBinding
import com.aijia.framework.ext.saveAs
import com.aijia.framework.ext.saveAsUnChecked
import java.lang.reflect.ParameterizedType

/**
 * @author Aijia
 * @date   2024/4/8
 * @desc   dataBinding Fragment 基类
 */
abstract class BaseDataBindFragment<VB: ViewBinding>: BaseFragment() {
    //This property is only valid between onCreateView and onDestroyView.
    var mBinding: VB? = null

    override fun getContentView(inflater: LayoutInflater, container: ViewGroup?): View {
        //mBinding = DataBindingUtil.inflate(inflater, getLayoutResId(), container, false)
        val type = javaClass.genericSuperclass
        val vbClass: Class<VB> = type!!.saveAs<ParameterizedType>().actualTypeArguments[0].saveAs()
        val method = vbClass.getDeclaredMethod("inflate", LayoutInflater::class.java)
        mBinding = method.invoke(this, layoutInflater)!!.saveAsUnChecked()
        return mBinding!!.root
    }

    override fun getLayoutResId(): Int = 0

    override fun onDestroy() {
        super.onDestroy()
        mBinding = null
    }
}