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

