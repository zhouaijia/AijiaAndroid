### 简介

OEM应用程序是指由制造商预装在Android设备上的特定应用程序。一些应用程序属于系统应用程序，需要使用系统级权限的API。这些权限和API允许应用程序与Android操作系统深度交互，并访问高级功能。系统级权限授予应用程序特权访问以执行特定操作或访问敏感资源，比如修改系统设置、访问受保护的存储、管理网络连接、控制硬件功能等。

通常，厂商使用Android.bp AOSP构建配置文件和Soong构建系统来构建OEM应用程序。这样可以让厂商在Android设备上预装他们的应用程序。

Android.bp文件提供了一种声明性的方式来定义模块、它们的属性、依赖关系和其他构建相关的信息。以下是Android.bp文件中常见字段的示例：

```
android_app_import {
    name: "Launcher",
    srcs: ["src/**/*.java"],
    resource_dirs: ["res"],
    platform_apis: false,
    certificate: "shared",
    privileged: false,
    static_libs: [
        "androidx.appcompat:appcompat",
    ],
    ...
}
```

- `android_app_import` — 定义模块类型。
- `name: "Launcher"` — 指定模块唯一名称，用于在构建系统中标识模块。
- `srcs:["src/**/*.java"]` — 包含构建模块时指定目录中的所有Java源文件，也可以是Kotlin文件。
- `resource_dirs:["res"]` — 为特定模块指定资源目录。
- `platform_apis: false` — 表示模块不需要访问Android平台提供的特定资源和功能，以防止使用特定于平台的函数和API。
- `certificate: "shared"` — 表示模块将使用共享证书进行签名，通常用于第三方或用户安装的应用程序和库。
- `privileged: false` — 表示模块不需要特殊权限或对系统级资源的访问，视为Android系统中的常规应用程序或组件。
- `static_libs:` — 指定模块在Android构建过程中依赖的静态库，这些库是gradle.build文件中的依赖项。

在开发OEM应用程序时，以Launcher应用程序为例，可能需要使用具有签名或系统权限的系统相关API，比如绑定小部件时可能会需要使用`AppWidgetManager#bindAppWidgetIdIfAllowed(int appWidgetId, ComponentName provider)`方法。此方法需要具有"signature|privileged"保护级别的权限。

我们可以在AOSP AndroidManifest.xml中找到所有权限的描述，这些权限会因Android版本而异。

"signature|privileged"表示此权限的保护级别，系统只向具有与声明权限的应用程序相同证书签名的应用程序授予此权限。

### OEM应用获取系统签名权限

为了授予该权限，有两种选择：

1. 应用程序应该使用与声明权限的应用程序相同的证书进行签名。
2. 将APK安装到Android系统镜像中的专用文件夹中。

从Launcher的Android.bp文件中，我们可以通过设置以下属性轻松授予`android.permission.BIND_APPWIDGET`权限并使用系统相关API：

- `certificate: "platform"` — 使用平台证书签名并授予签名权限，适用于系统级组件、Android框架、系统应用程序和核心系统库。

或者

- `privileged: true` — APK将安装在Android设备的`/system/priv-app/`分区中，并授予签名或系统权限，专为具有提升访问权限和权限的特权系统应用程序设计。
- `required: ["privapp_permissions.xml"]` — 表示模块需要一个`privapp_permissions.xml`文件来定义其特权应用程序权限，通常位于Android系统分区的`/etc/permissions`目录中，包含"signature|privileged"权限。

### 给App使用platform签名

使用Gradle构建APK使得我们可以在Android Studio中安装和运行APK，并在XML Android Studio中查看布局设计更改，就像它们是第三方应用程序一样。而且，通过Gradle构建的APK可以安装到特权分区`/system/priv-app/`，这对于没有完整的AOSP构建访问权限但仍需要在系统上安装、运行和测试APK的情况非常有用。

下面介绍一种方法，即“应用程序应该使用与声明权限的应用程序相同的证书进行签名”。我们可以使用以下步骤来生成签名密钥库（keystore）并授予签名或signatureOrSystem权限：

1. **安装Java开发工具包（JDK）**：如果尚未安装，请先安装JDK。

2. **打开终端或命令提示符**：导航到存放`platform.x509.pem`和`platform.pk8`文件的目录。为了方便起见，建议将所有文件放在同一个文件夹中。

3. **将`platform.pk8`文件转换为DER格式**：运行以下命令：

```
openssl pkcs8 -inform DER -nocrypt -in platform.pk8 -out platform.key
```

4. **生成PKCS12文件**：通过合并X.509证书和私钥，为证书分配名称并指定密码，生成PKCS12文件。运行以下命令：

```
openssl pkcs12 -export -in platform.x509.pem -inkey platform.key -name platform -out platform.pem -password pass:password
```

5. **导入PKCS12密钥库文件**：将名为`platform.pem`的PKCS12密钥库文件导入到名为`platform.keystore`的新密钥库文件中，并指定密码。运行以下命令：

```
keytool -importkeystore -destkeystore platform.keystore -deststorepass password -srckeystore platform.pem -srcstoretype PKCS12 -srcstorepass password
```

6. **在项目根目录创建`/keystore`目录**：将生成的`platform.keystore`文件放入该目录中。

7. **在模块的`build.gradle`文件中添加调试（debug）或发布（release）签名配置**：

```
android {
    def keystore_path = project.rootProject.getProjectDir().toString() + "/keystore/platform.keystore"

    signingConfigs {
        debug {
            storeFile file(keystore_path)
            keyAlias 'platform'
            storePassword 'password'
            keyPassword 'password'
        }
    }

    buildTypes {
        debug {
            signingConfig signingConfigs.debug
        }
    }
}
```

8. **构建并运行项目**：在Android Studio中选择菜单项`Build` → `Rebuild project`，然后点击运行按钮。至此，您的OEM应用程序将被授予签名或signatureOrSystem权限。

以上是一种为使用系统相关API的OEM应用程序使用Gradle构建的方法，通过签名和权限配置实现了系统级访问权限的授予。

### App安装到系统特定目录

让我们考虑第二个选项：“将应用程序放置到Android系统镜像上的专用文件夹中”。将应用程序安装在/system/priv-app/目录下作为特权应用程序，只会授予特权应用程序具有签名或系统级别保护的权限[1]。一般来说，我们需要使用Gradle构建APK（作为第三方应用程序，无需平台密钥），然后将APK推送到特权分区，并将所需的权限添加到privapp_permissions.xml文件中。

以下是如何在带有用户调试固件版本的设备上手动安装系统APK的步骤：

首先，授权设备的root访问权限并重新挂载系统分区，以允许对系统文件进行读写访问。简而言之，将/system分区设置为可写状态：

```
adb root
adb remount
adb shell "su 0 mount -o rw,remount /system" # adb shell "su 0 mount -o rw,remount /"
```

创建目录并将APK推送到/system/priv-app/分区：

```
adb shell mkdir /system/priv-app/Launcher
adb push Launcher.apk /system/priv-app/Launcher
```

修改Android设备上APK文件的权限。这将为所有者授予读写权限，并对组和其他用户设置为只读权限：

```
adb shell chmod 644 /system/priv-app/Launcher/Launcher.apk
```

同样重要的是，在Android系统中的privapp_permissions.xml文件中添加所需的权限。首先，拉取文件：

```
adb pull /etc/permissions/privapp-permissions-platform.xml .
```

编辑privapp_permissions.xml文件，内容如下：

```
<?xml version="1.0" encoding="utf-8"?>
<permissions>
    ...
    <privapp-permissions package="com.example.launcher">
        <permission name="android.permission.BIND_APPWIDGET"/>
    </privapp-permissions>
</permissions>
```

确保包名称与APK中的com.example.launcher一致。添加权限android.permission.BIND_APPWIDGET后，将文件推送到同一路径：

```
adb push privapp-permissions-platform.xml /etc/permissions/
```

重新启动设备，这样您的APK将被授予signatureOrSystem权限：

```
adb reboot
```

顺便提一下：使用此方法完成OEM应用程序开发后，我们仍然可以将该APK集成到AOSP构建中，并使其预装。有两种将APK添加到AOSP构建系统的方法：添加完整项目或将已编译的APK视为示例。

因此，我们可以临时使用这种签名和安装方法来开发、安装、运行和测试使用与系统相关的OEM应用程序。

优点：实时查看布局设计更改、快速安装和卸载APK，允许授予签名或signatureOrSystem权限。

缺点：Gradle依赖项可能缺少库（例如Car库），因此可能需要将AOSP中的已编译库添加到Android项目中。此外，某些API可能会被隐藏，因此在这种情况下，可以在项目中创建存根或使用反射。

注意：访问系统权限和API需要承担额外的安全和隐私责任。确保您的应用程序符合处理敏感数据、保护通信渠道和尊重用户隐私的最佳实践。

缩写名称含义

- OEM：原始设备制造商
- APK：Android应用程序包
- AOSP：Android开源项目
- keystore：Java中用于存储加密密钥（包括私钥和证书）的特定文件格式