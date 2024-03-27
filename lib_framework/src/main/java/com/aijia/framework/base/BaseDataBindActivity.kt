package com.aijia.framework.base

import android.view.LayoutInflater
import androidx.viewbinding.ViewBinding
import com.aijia.framework.ext.saveAs
import com.aijia.framework.ext.saveAsUnChecked
import java.lang.reflect.ParameterizedType

/**
 * @author Aijia
 * @date   2024/3/14
 * @desc   dataBinding Activity基类
 */
abstract class BaseDataBindActivity<DB : ViewBinding> : BaseActivity() {
    lateinit var mBinding: DB

    override fun setContentLayout() {
//      mBinding = DataBindingUtil.setContentView(this, getLayoutResId())
        val type = javaClass.genericSuperclass
        val vbClass: Class<DB> = type!!.saveAs<ParameterizedType>().actualTypeArguments[0].saveAs()
        val method = vbClass.getDeclaredMethod("inflate", LayoutInflater::class.java)
        mBinding = method.invoke(this, layoutInflater)!!.saveAsUnChecked()
        setContentView(mBinding.root)
    }

    override fun getLayoutResId(): Int = 0
}