## APP壳工程：
1. 自定义Application类，包含前后台切换监测、以Task形式初始化三方库、dex分包、异常拦截等功能
2. 打包环境
3. 签名
4. 混淆规则
5. 业务模块集成
6. APP主题等配置，这里需要注意Android 5.0和8.0的 全屏、沉浸式、窗口背景透明问题，需要增加values-v21、
values-v26、values-v27文件夹做单独处理
7. 前后台切换时，SplashActivity重复启动问题：启动模式若是singleTask就会重复启动，若是standard，则正常