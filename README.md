## 技术栈
Kotlin+协程+Flow+Retrofit+Jetpack+MVVM+组件化+模块化

## 项目简介
- 项目采用 Kotlin 语言编写，结合 Jetpack 相关控件，Navigation，Lifecyle，DataBinding，LiveData，ViewModel等搭建的 MVVM 架构模式；
- 通过组件化，模块化拆分，实现项目更好解耦和复用，ARouter 实现模块间通信；
- 使用 协程+Flow+Retrofit+OkHttp 优雅地实现网络请求；
- 通过 mmkv，Room 数据库等实现对数据缓存的管理；

## 项目主要基础功能组件
- Log：LogUtils（无需传入TAG，其内部可自动反射拿到当前的类名）
- SharePreference：系统原生的SharePreference足以满足大多数的需求，若追求极致性能可选择MMKV
- Network：Retrofit+OkHttp，需进一步做扩展优化
- WebView：支持JSBridge
- Singleton: Java语言采用Holder模式的单例，Kotlin采用object（或lazy）式单例

## 项目主要功能模块
- Account：登录（用户名密码登录、验证码登录）、注册、修改密码登
- 蓝牙通信
- 地图