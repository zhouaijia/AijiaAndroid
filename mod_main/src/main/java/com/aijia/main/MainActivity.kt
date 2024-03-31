package com.aijia.main

import android.os.Bundle
import android.view.MenuItem
import androidx.navigation.NavController
import androidx.navigation.NavGraph
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavGraphNavigator
import androidx.navigation.NavOptions
import androidx.navigation.NavigatorProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import com.aijia.framework.base.BaseDataBindActivity
import com.aijia.framework.toast.TipsToast
import com.aijia.framework.utils.AppExit
import com.aijia.main.databinding.ActivityMainBinding
import com.aijia.main.navigator.FixFragmentNavigator
import com.aijia.main.ui.category.CategoryFragment
import com.aijia.main.ui.home.HomeFragment
import com.aijia.main.ui.mine.MineFragment
import com.aijia.main.ui.system.SystemFragment


class MainActivity : BaseDataBindActivity<ActivityMainBinding>() {

    private lateinit var mNavController: NavController

    override fun initView(savedInstanceState: Bundle?) {

        // 1.找到NavController、NavHostFragment
        mNavController = findNavController(R.id.nav_host_fragment_activity_main)
        val navHostFragment = supportFragmentManager.
            findFragmentById(R.id.nav_host_fragment_activity_main) as NavHostFragment

        // 2.自定义FragmentNavigator，mobile_navigation.xml中的fragment标签改为sumFragment
        val fragmentNavigator = FixFragmentNavigator(this, navHostFragment.childFragmentManager, navHostFragment.id)
7
        // 3.将自定义的FragmentNavigator注册到NavController中
        mNavController.navigatorProvider.addNavigator(fragmentNavigator)

        // 4.设置navGraph：为防止重复走生命周期方法，需将fragment中的app:navGraph="@navigation/mobile_navigation"移除
        //mNavController.setGraph(createNavGraph(mNavController.navigatorProvider, fragmentNavigator), null)
        mNavController.setGraph(R.navigation.mobile_navigation) //也可以通过xml静态资源设置

        // 5.将NavController和BottomNavigationView进行绑定，形成联动效果
        //mBinding.bottomNavView.setupWithNavController(mNavController)
        mBinding.bottomNavView.setOnItemSelectedListener { item ->
            // 避免再次点击重复创建
            if (item.isChecked) return@setOnItemSelectedListener true

            return@setOnItemSelectedListener onNavDestinationSelected(item, mNavController)
        }
    }

    private fun onNavDestinationSelected(item: MenuItem, navController: NavController): Boolean {
        val builder = NavOptions.Builder().setLaunchSingleTop(true).setRestoreState(true)

        // 下面代码会导致错误：
        // java.lang.IllegalArgumentException: saveBackStack("901be899") must be self contained and not
        // reference fragments from non-saved FragmentTransactions. Found reference to fragment HomeFragment{de0456a}
        // in BackStackEntry{...} that were previously added to the FragmentManager through a separate FragmentTransaction.
        // 出错原因是hide和show方法
        /*if (item.order and Menu.CATEGORY_SECONDARY == 0) {
            // 不管当前要导航去的是哪一个fragment，都将其在栈中的下一个fragment设置为homeFragment
            // 需要注意的是，当saveState为false时，在backStack中，目标fragment与homeFragment之间所有的fragment
            // 都会被清除，包括其state，这样当再次进入各个fragment时就会重新走创建流程，如此自定义的FragmentNavigator就会失效
            builder.setPopUpTo(
                navController.graph.findStartDestination().id,
                inclusive = false,
                // 若将saveState设置为false，那么切换fragment就不会出错，但是自定义的FragmentNavigator就会失效
                saveState = true
            )
        }*/

        val options = builder.build()
        return try {
            navController.navigate(item.itemId, null, options)
            // Return true only if the destination we've navigated to matches the MenuItem
            navController.currentDestination?.id == item.itemId
        } catch (e: IllegalArgumentException) {
            false
        }
    }


    override fun onBackPressed() {
        // super.onBackPressed()
        // if (mCurFragment?.onBackPressed() == true) {
        //     return
        // }

        val startDestinationId = mNavController.graph.findStartDestination().id
        // 先回到home页
        if (mBinding.bottomNavView.selectedItemId != startDestinationId) {
            mBinding.bottomNavView.selectedItemId = startDestinationId
        } else {
            AppExit.onBackPressed(this, { TipsToast.showTips(getString(R.string.app_exit_one_more_press)) })
        }
    }

    /**
     * 也可以在kotlin代码中动态创建 navGraph，即使用自定义的导航器来创建各个Destination
     */
    private fun createNavGraph(
        provider: NavigatorProvider,
        fragmentNavigator: FixFragmentNavigator
    ): NavGraph {
        val navGraph = NavGraph(NavGraphNavigator(provider))

        val destHome = fragmentNavigator.createDestination()
        destHome.id = R.id.nav_home
        destHome.setClassName(HomeFragment::class.java.canonicalName!!)
        navGraph.addDestination(destHome)

        val destCategory = fragmentNavigator.createDestination()
        destCategory.id = R.id.nav_category
        destCategory.setClassName(CategoryFragment::class.java.canonicalName!!)
        navGraph.addDestination(destCategory)

        val destSystem = fragmentNavigator.createDestination()
        destSystem.id = R.id.nav_system
        destSystem.setClassName(SystemFragment::class.java.canonicalName!!)
        navGraph.addDestination(destSystem)

        val destMine = fragmentNavigator.createDestination()
        destMine.id = R.id.nav_mine
        destMine.setClassName(MineFragment::class.java.canonicalName!!)
        navGraph.addDestination(destMine)

        //设置默认选中的fragment页面
        navGraph.setStartDestination(destHome.id)

        return navGraph
    }
}