Android 系统服务在向高级应用程序暴露硬件和 Linux 内核的低级功能方面起着关键作用。 相较于普通的 Android 服务，系统服务
是由 SystemServer 启动的，因此它们作为系统进程运行，拥有普通 Android 服务无法获得的额外特权。

要创建一个系统服务，我们需要以下组件：

- AIDL 服务接口
- 承载服务的应用程序
- 简单客户端测试程序

### 步骤 1：创建 AIDL 和Stubs

AIDL（Android Interface Definition Language）允许你定义客户端和服务之间共同约定的编程接口，以便使用进程间通信（IPC）进行通信。

更多细节： [Android Interface Definition Language (AIDL)] (https://stuff.mit.edu)

我们需要创建一个带有生成Stub的 AIDL 接口。

### 步骤 2：

现在我们需要创建服务类，该类将扩展我们的存根以获取回调。

```
public class AijiaService extends IAijiaServiceManagerAidlInterface.Stub {
    private final static String LOG_TAG = "AijiaService";

    private final Object mLock = new Object();

    private final Context mContext;

    AijiaService(Context context) {
        mContext = context;
    }

    @Override
    public void blinkBulb() throws RemoteException {
        Slog.i(LOG_TAG,"blinkBulb blinkBulb");
    }
}
```

### 步骤 3：

通过在 `SystemServiceRegistry.java` 中注册我们的 `SystemService `并将其添加到 `SystemServer.java` 中。

在`frameworks/base/core/java/android/app/SystemServiceRegistry.java`中注册服务：

```
registerService(Context.AIJIA_SERVICE, AijiaServiceManager.class,
        new CachedServiceFetcher<AijiaServiceManager>() {
            @Override
            public AijiaServiceManager createService(ContextImpl ctx) throws ServiceNotFoundException {
                IBinder binder;
                if (true){//ctx.getApplicationInfo().targetSdkVersion >= Build.VERSION_CODES.O) {
                    binder = ServiceManager.getServiceOrThrow(Context.AIJIA_SERVICE);
                } else {
                    binder = ServiceManager.getService(Context.AIJIA_SERVICE);
                }
                return new AijiaServiceManager(ctx,IAijiaServiceManagerAidlInterface.Stub.asInterface(binder));
            }});
```

在 `SystemServer.java `中添加你的服务至 `startBootstrapServices()`。你会在那里找到其他默认的系统服务。

```
//frameworks/base/services/java/com/android/server/SystemServer.java
AijiaService androidservice = null;
try{
    traceBeginAndSlog("AijiaService");
    androidservice = new AijiaService(mSystemContext);
    ServiceManager.addService(Context.AIJIA_SERVICE,androidservice);
}catch(Throwable e){
    Slog.e(TAG, "Starting AijiaService failed!!! ", e);
}
traceEnd();
```

别忘了在 `Context.java `中声明它的名称：

```
//frameworks/base/core/java/android/content/Context.java
public static final String AIJIA_SERVICE = "aijiaservice";
```

### 步骤 4：

编译出错，这是因为你没有更新current API, 在构建镜像之前，你需要让系统知道有一个新的Stub API。

运行命令：`make api-stubs-docs-update-current-api`

### 步骤 5：

运行时出现SELinux 拒绝服务，这是因为未添加sepolicy。

```
SELinux：avc: denied { add }
```

我们需要添加 SELinux 规则来允许该服务。

路径：`system/sepolicy/private/service_contexts`

```
aijiaservice                    u:object_r:aijia_service:s0 //your service name
```

以上就是在 AOSP 中添加系统服务的详细步骤。文章具体代码请参考下面的github链接。