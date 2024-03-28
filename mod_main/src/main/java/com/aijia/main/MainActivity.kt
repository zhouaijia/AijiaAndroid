package com.aijia.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.aijia.framework.base.BaseDataBindActivity
import com.aijia.main.databinding.ActivityMainBinding

class MainActivity : BaseDataBindActivity<ActivityMainBinding>() {

    private lateinit var mNavController: NavController

    override fun initView(savedInstanceState: Bundle?) {

        // 找到NavController、NavHostFragment
        mNavController = findNavController(R.id.nav_host_fragment_activity_main)

        // 将NavController和BottomNavigationView进行绑定，形成联动效果
        mBinding.bottomNavView.setupWithNavController(mNavController)
    }
}