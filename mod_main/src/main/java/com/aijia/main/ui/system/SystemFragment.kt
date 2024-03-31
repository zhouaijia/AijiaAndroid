package com.aijia.main.ui.system

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.aijia.log.ALog
import com.aijia.main.R
import com.aijia.main.ui.system.viewmodel.SystemViewModel

class SystemFragment : Fragment() {

    companion object {
        fun newInstance() = SystemFragment()
    }

    private lateinit var viewModel: SystemViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        ALog.i("------------------体系--------------->onCreateView")
        return inflater.inflate(R.layout.fragment_system, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(SystemViewModel::class.java)
        // TODO: Use the ViewModel
    }

}