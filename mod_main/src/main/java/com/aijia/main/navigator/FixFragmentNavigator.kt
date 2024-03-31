package com.aijia.main.navigator

import android.content.Context
import android.util.Log
import androidx.annotation.IdRes
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.Lifecycle
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavOptions
import androidx.navigation.Navigator
import androidx.navigation.fragment.FragmentNavigator

/**
 * 问题：FragmentNavigator是通过 ft.replace()方法添加Fragment的，replace会导致生命周期重走；
 * 又因为里面需要使用到mBackStack后退栈，但是可见性是private，所以子类中是无法使用的。
 * 解决方案：
 * 重写FragmentNavigator.navigate()方法，通过反射获取mBackStack后退栈；
 * 将显示Fragment.replace()改成hide()和show()方法
 *
 * 需要在类头添加Navigator.Name注解，添加一个名称
 *
 * https://blog.csdn.net/datian1234/article/details/127129122
 */
@Navigator.Name("fixFragment")
class FixFragmentNavigator(
    private val mContext: Context,
    private val mFragmentManager: FragmentManager,
    private val mContainerId: Int
) : FragmentNavigator(mContext, mFragmentManager, mContainerId) {
    companion object {
        const val TAG = "FixFragmentNavigator"
    }


    /**
     * Navigation 2.3.5版本
     */
    /*override fun navigate(
        destination: Destination,
        args: Bundle?,
        navOptions: NavOptions?,
        navigatorExtras: Navigator.Extras?
    ): NavDestination? {
        ALog.w(TAG, "-----------navigate----------->")
        if (mFragmentManager.isStateSaved) {
            Log.w(TAG, "Ignoring navigate() call: FragmentManager has already saved it's state")
            return null
        }
        var className = destination.className
        if (className[0] == '.') {
            className = mContext.packageName + className
        }
        val ft: FragmentTransaction = mFragmentManager.beginTransaction()

        var enterAnim = navOptions?.enterAnim ?: -1
        var exitAnim = navOptions?.exitAnim ?: -1
        var popEnterAnim = navOptions?.popEnterAnim ?: -1
        var popExitAnim = navOptions?.popExitAnim ?: -1
        if (enterAnim != -1 || exitAnim != -1 || popEnterAnim != -1 || popExitAnim != -1) {
            enterAnim = if (enterAnim != -1) enterAnim else 0
            exitAnim = if (exitAnim != -1) exitAnim else 0
            popEnterAnim = if (popEnterAnim != -1) popEnterAnim else 0
            popExitAnim = if (popExitAnim != -1) popExitAnim else 0
            ft.setCustomAnimations(enterAnim, exitAnim, popEnterAnim, popExitAnim)
        }

        // ft.replace(mContainerId, frag) //去掉replace()方法

        // 1.先查询当前显示的fragment,不为空则将其hide
        val fragment = mFragmentManager.primaryNavigationFragment //当前显示的fragment
        fragment?.let { ft.hide(it) }

        var frag: Fragment?
        val tag = destination.id.toString()
        frag = mFragmentManager.findFragmentByTag(tag)

        // 2.根据tag查询当前添加的fragment是否不为null，不为null则将其直接show
        if (frag != null) {
            ft.show(frag)

            // 3.为null则通过instantiateFragment方法创建fragment实例
        } else {
            frag = mFragmentManager.fragmentFactory.instantiate(mContext.classLoader, className)
            frag.arguments = args
            //if (!frag.isAdded) {
            ft.add(mContainerId, frag, tag)
            //}
        }
        ft.setPrimaryNavigationFragment(frag)


        *//*-----通过反射的方式获取mBackStack------*//*
        val mBackStack: ArrayDeque<Int>
        val field = FragmentNavigator::class.java.getDeclaredField("mBackStack")
        field.isAccessible = true
        mBackStack = field.get(this) as ArrayDeque<Int>

        ALog.w(TAG, "------navigate----->mBackStack: "+mBackStack.size)
        val initialNavigation = mBackStack.isEmpty()
        @IdRes val destId = destination.id
        val isSingleTopReplacement = (navOptions != null && !initialNavigation
                && navOptions.shouldLaunchSingleTop()
                && mBackStack.peekLast() == destId)

        val isAdded: Boolean
        if (initialNavigation) {
            isAdded = true
        } else if (isSingleTopReplacement) {
            // Single Top means we only want one instance on the back stack
            if (mBackStack.size > 1) {
                // If the Fragment to be replaced is on the FragmentManager's
                // back stack, a simple replace() isn't enough so we
                // remove it from the back stack and put our replacement
                // on the back stack in its place
                mFragmentManager.popBackStack(
                    generateBackStackName(mBackStack.size, mBackStack.peekLast()),
                    FragmentManager.POP_BACK_STACK_INCLUSIVE
                )
                ft.addToBackStack(generateBackStackName(mBackStack.size, destId))
            }
            isAdded = false
        } else {
            ft.addToBackStack(generateBackStackName(mBackStack.size + 1, destId))
            isAdded = true
        }
        if (navigatorExtras is Extras) {
            val extras = navigatorExtras as Extras?
            for ((key, value) in extras!!.sharedElements) {
                ft.addSharedElement(key, value)
            }
        }

        // 4.将创建的实例添加在事务中
        ft.setReorderingAllowed(true)
        ft.commit()

        return if (isAdded) {
            mBackStack.add(destId)
            destination
        } else {
            null
        }
    }

    /**
     * 生成回退栈名称
     */
    private fun generateBackStackName(backIndex: Int, destId: Int): String {
        return "$backIndex - $destId"
    }
    */



    // 2.5.3版本，就算改了也有问题，BottomNavigationView联合NavController一起使用时，总是会报错
    // java.lang.IllegalArgumentException: saveBackStack("901be899") must be self contained and not
    // reference fragments from non-saved FragmentTransactions. Found reference to fragment HomeFragment{de0456a}
    // in BackStackEntry{...} that were previously added to the FragmentManager through a separate FragmentTransaction.

    // Navigation 2.5.3版本
    private var savedIds: MutableSet<String>? = null
    init {
        try {
            val field = FragmentNavigator::class.java.getDeclaredField("savedIds")
            field.isAccessible = true
            savedIds = field[this] as MutableSet<String>
        } catch (e: Exception) {
            Log.d(TAG, "反射获取SavedIds失败: $e")
        }
    }

    //Navigation 2.5.3版本
    override fun navigate(
        entries: List<NavBackStackEntry>,
        navOptions: NavOptions?,
        navigatorExtras: Navigator.Extras?
    ) {
        if (mFragmentManager.isStateSaved) {
            Log.i(TAG, "Ignoring navigate() call: FragmentManager has already saved its state")
            return
        }
        for (entry in entries) {
            navigate(entry, navOptions, navigatorExtras)
        }
    }

    private fun navigate(
        entry: NavBackStackEntry,
        navOptions: NavOptions?,
        navigatorExtras: Navigator.Extras?
    ) {
        val backStack = state.backStack.value
        val initialNavigation = backStack.isEmpty()
        val restoreState = (
                navOptions != null
                        && !initialNavigation
                        && navOptions.shouldRestoreState()
                        && savedIds?.remove(entry.id) == true
                )
        if (restoreState) {
            // Restore back stack does all the work to restore the entry
            mFragmentManager.restoreBackStack(entry.id)
            state.push(entry)
            return
        }
        val ft = createFragmentTransaction(entry, navOptions)

        val destination = entry.destination as Destination
        @IdRes val destId = destination.id
        //Build first class singleTop behavior for fragments
        val isSingleTopReplacement = (
                navOptions != null && !initialNavigation &&
                        navOptions.shouldLaunchSingleTop() &&
                        backStack.last().destination.id == destId
                )
        val isAdded = when {
            initialNavigation -> {
                Log.d(TAG, "-------------navigate-----------initialNavigation is true")
                true
            }
            isSingleTopReplacement -> { //一般不会执行
                Log.d(TAG, "-------------navigate-----------isSingleTopReplacement is true")
                // Single Top means we only want one instance on the back stack
                if (backStack.size > 1) {
                    // If the Fragment to be replaced is on the FragmentManager's
                    // back stack, a simple replace() isn't enough so we
                    // remove it from the back stack and put our replacement
                    // on the back stack in its place
                    mFragmentManager.popBackStack(
                        entry.id,
                        FragmentManager.POP_BACK_STACK_INCLUSIVE
                    )
                    ft.addToBackStack(entry.id)
                }
                false
            }
            else -> {
                Log.d(TAG, "------navigate----->initialNavigation is false, backStackSize: "+backStack.size)
                ft.addToBackStack(entry.id) // entry.id每次都不同
                true
            }
        }

        // if (!initialNavigation) {
        //     ft.addToBackStack(entry.id)
        // }

        if (navigatorExtras is Extras) {
            for ((key, value) in navigatorExtras.sharedElements) {
                ft.addSharedElement(key, value)
            }
        }

        // 4.将创建的实例添加在事务中
        ft.commit()
        // The commit succeeded, update our view of the world
        if (isAdded) state.push(entry)
    }

    private fun createFragmentTransaction(
        entry: NavBackStackEntry,
        navOptions: NavOptions?
    ): FragmentTransaction {
        Log.i(TAG, "-------------createFragmentTransaction-----------")
        val destination = entry.destination as Destination
        val args = entry.arguments
        var className = destination.className
        if (className[0] == '.') {
            className = mContext.packageName + className
        }
        val ft = mFragmentManager.beginTransaction()
        var enterAnim = navOptions?.enterAnim ?: -1
        var exitAnim = navOptions?.exitAnim ?: -1
        var popEnterAnim = navOptions?.popEnterAnim ?: -1
        var popExitAnim = navOptions?.popExitAnim ?: -1
        if (enterAnim != -1 || exitAnim != -1 || popEnterAnim != -1 || popExitAnim != -1) {
            enterAnim = if (enterAnim != -1) enterAnim else 0
            exitAnim = if (exitAnim != -1) exitAnim else 0
            popEnterAnim = if (popEnterAnim != -1) popEnterAnim else 0
            popExitAnim = if (popExitAnim != -1) popExitAnim else 0
            ft.setCustomAnimations(enterAnim, exitAnim, popEnterAnim, popExitAnim)
        }

        //val frag = mFragmentManager.fragmentFactory.instantiate(mContext.classLoader, className)
        //frag.arguments = args
        //ft.replace(mContainerId, frag)

        // 1.先查询当前显示的fragment,不为空则将其hide
        var frag = mFragmentManager.primaryNavigationFragment
        if (frag != null) {
            ft.setMaxLifecycle(frag, Lifecycle.State.STARTED)
            ft.hide(frag)
        }

        val tag = destination.id.toString()
        frag = mFragmentManager.findFragmentByTag(tag)

        // 2.根据tag查询当前添加的fragment是否不为null，不为null则将其直接show
        if (frag != null) {
            ft.setMaxLifecycle(frag, Lifecycle.State.RESUMED)
            ft.show(frag)
        } else {
            // 3.为null则通过instantiateFragment方法创建fragment实例
            frag = mFragmentManager.fragmentFactory.instantiate(mContext.classLoader, className)
            frag.arguments = args
            ft.add(mContainerId, frag, tag)
        }

        ft.setPrimaryNavigationFragment(frag)
        ft.setReorderingAllowed(true)

        return ft
    }

}