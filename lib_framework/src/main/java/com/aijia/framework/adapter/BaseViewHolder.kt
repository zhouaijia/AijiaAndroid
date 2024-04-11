package com.aijia.framework.adapter

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding

/**
 * @author Aijia
 * @date 2024/4/8
 * @desc   基本ViewHolder
 */
open class BaseViewHolder(rootView: View) : RecyclerView.ViewHolder(rootView)

open class BaseBindViewHolder<B : ViewBinding>(val binding: B) : BaseViewHolder(binding.root)