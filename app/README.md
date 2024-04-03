## APP壳工程：
1. 自定义Application类，包含前后台切换监测、以Task形式初始化三方库、dex分包、异常拦截等功能
2. 打包环境
3. 签名
4. 混淆规则
5. 业务模块集成
6. APP主题等配置，这里需要注意Android 5.0和8.0的 全屏、沉浸式、窗口背景透明问题，需要增加values-v21、
values-v26、values-v27文件夹做单独处理
7. 前后台切换时，SplashActivity重复启动问题：启动模式若是singleTask就会重复启动，若是standard，则正常
8. app壳模块和其它模块，都需要设置dataBinding=true，否则其它模块会报错（找不到DataBinderMapperImpl）
9. 使用ARouter时，可能出现以下问题：
   - There is no route match the path [/arouter/service/interceptor]
   - There is no route match the path [/arouter/service/autowired]
   - No mapping files were found, check your configuration please
   - 或者使用@AutoWired注解注入失败
   - 新增页面之后，无法跳转
   解决方案：
   - 将App重新覆盖安装一遍，或卸载重装
   - build.gradle中的混淆是否打开了，如果打开了，则ARouter相关的一些类是不可以混淆的
   - 检查各个build.gradle文件中的ARouter配置是否正确
   - Kotlin与Java版的ARouter配置是不一样的
   - ARouter加载Dex中的映射文件会有一定耗时，所以ARouter会缓存映射文件，直到新版本升级(版本号或者versionCode变化)，
     而如果是开发版本(ARouter.openDebug())， ARouter 每次启动都会重新加载映射文件，开发阶段一定要打开 Debug 功能

