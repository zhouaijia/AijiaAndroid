# WebView 组件

### 缺点
不可用于xml布局中，因为AWebView的父类并不是一个View或者ViewGroup。

真正的WebView需要通过 AWebView.getView() 方法获取。

除了腾讯 X5 WebView和原生 WebView，若想添加其它WebView库，则需要在目录 webkit/view 下面增加相应的类实现。