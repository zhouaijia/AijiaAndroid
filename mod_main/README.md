## main模块

#### 主要功能
1. 首页
2. 底部导航

#### 闪屏页重复出现问题
前后台切换时，SplashActivity页面重复出现的问题：设置其启动模式为standard

#### Jetpack中Navigation的使用步骤：
- 创建bottom_nav_menu.xml，配置导航中各按钮的图标和名字； 
- 创建mobile_navigation.xml文件，配置导航各按钮对应的Fragment，以及首先被启动的Fragment
- activity_main.xml布局的BottomNavigationView标签下：app:menu="@menu/bottom_nav_menu"
- activity_main.xml布局的fragment标签下：app:navGraph="@navigation/mobile_navigation"
- Activity中将NavController和BottomNavigationView进行绑定，形成联动效果

以上配置完成后，Navigation就可以正常使用了。
但是，FragmentNavigator是通过replace()方法添加Fragment的，replace方式会导致生命周期重走，所以这里需要优化一下。

#### Navigation的优化
- 自定义FragmentNavigator，如FixFragmentNavigator，同时需将mobile_navigation.xml中的fragment标签改为sumFragment
- 重写FragmentNavigator.navigate()方法，通过反射获取mBackStack后退栈、将Fragment.replace方法替换成hide和show方法
- 处理WindowInsets问题
- 将自定义的FixFragmentNavigator注册到NavController中
- 代码中动态设置navGraph：将fragment中的app:navGraph="@navigation/mobile_navigation"移除
- 后期考虑摒弃mobile_navigation.xml，改用注解标记路由节点
- 后期考虑可配置化的底部导航，即可通过json数据进行动态配置

#### 问题处理
1. 找不到 sumFragment 标签、FixFragmentNavigator未添加。
   解决方法：activity_main.xml布局的fragment标签下，删除app:navGraph="@navigation/mobile_navigation"
2. navigation2.3.5与2.5.3版本不同，2.5.3做了很大的升级改动，其中FragmentNavigator改动比较大，所以之前优化时自定义的
   FixFragmentNavigator失效，需要重新适配优化。
3. navigation2.5.3版本中，因为自定义的FixFragmentNavigator将replace方法换成了hide和show方法，所以会报错：
   java.lang.IllegalArgumentException: saveBackStack("901be899") must be self contained and not
   reference fragments from non-saved FragmentTransactions. Found reference to fragment HomeFragment{de0456a}
   in BackStackEntry{...} that were previously added to the FragmentManager through a separate FragmentTransaction.
4. navigation2.5.3版本中有个功能，就是不管当前要导航去的是哪一个fragment，都可将其在backStack中的下一个fragment
   设置为homeFragment，而homeFragment上面的所有fragment都会被清除。如果将setPopUpTo方法中的saveState参数设置为false，
   则backStack中所有fragment不仅会被清除，包括它们的state也会被清除，这样当再次进入各个fragment时就会重新走创建流程，
   如此自定义的FixFragmentNavigator就会失效。
5. 绑定底部导航BottomNavigationView与NavController进行点击导航按钮切换fragment联动：抛弃系统提供的方法
       BottomNavigationView.setupWithNavController(mNavController)
   改用 BottomNavigationView.setOnItemSelectedListener监听按钮点击事件，然后进行手动导航。
   优势：控制灵活，避免系统默认走setPopUpTo方法导致的IllegalArgumentException异常
   缺点：不能使用系统提供的api，有点繁琐；可能有内存泄露的风险（FragmentManager中的mBackStack一直在变大）