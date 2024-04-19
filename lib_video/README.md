
常见的三种播放场景：
- 短视频场景（Short Video）- 类似抖音首页竖版视频场景
- 中视频场景（Feed Video） - 类似西瓜视频 Feed 视频流场景
- 长视频场景（Long Video） - 类似爱奇艺/腾讯视频/优酷视频的电视剧/电影场景

针对短视频、中视频场景，提供了 `短视频场景控件`、`中视频场景控件` 进一步简化接入。 业务可将 `lib_video`
组件引入工程，添加数据源即可快速搭建播放场景，无需关心播放器如何使用。


### 业务层
#### 场景页面
场景页面 = 场景控件 + 业务 API 数据获取
* 短视频页面 - ShortVideoFragment
* 中视频页面 - FeedVideoFragment
* 长视频页面 - LongVideoFragment
* 视频详情页面 - DetailVideoFragment

### 场景控件层
#### 场景控件
场景控件 = 页面控件 + 下拉刷新控件 + 上拉加载控件
* 短视频场景控件 - ShortVideoSceneView
* 中视频场景控件 - FeedVideoSceneView
#### 页面控件
页面控件 = RecyclerView/ViewPager2 + 播放控件
* 短视频页面控件 - ShortVideoPageView
* 中视频页面控件 - FeedVideoPageView

### 播放控件层
#### 播放器接口层
* 播放源 - MediaSource
* 播放器 - Player
* 播放器默认实现 - AVPlayer
* 视频控件 - VideoView
* 播放流程 - PlaybackController
* 播放浮层 - VideoLayer
* 浮层管理 - VideoLayerHost
