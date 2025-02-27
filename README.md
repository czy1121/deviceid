# DeviceId

获取设备标识(widevineId/instanceId/androidId/oaid)，采集设备信息。 

- widevineId 不需要用户确认隐私政策，部分厂商(比如小米)恢复出厂设置后 会重新生成
- instanceId 不需要用户确认隐私政策，应用卸载重装/清除应用数据 会重新生成
- androidId 需要用户确认隐私政策，恢复出厂设置/签名密钥卸载后应用重新安装 会重新生成
- oaid 需要用户确认隐私政策，用户可重置或关闭，恢复出厂设置会重置
 

注：oaid 的获取方法参考 https://github.com/gzu-liyujiang/Android_CN_OAID
 
## 应用标识

同一设备不同应用获取的值不同，常用于统计分析。

不需要额外申请权限。

- **widevineId(Media Drm Id)** 通过安卓数字版权管理(DRM)框架获取的唯一设备ID
  - 不可重置，部分厂商(比如小米)恢复出厂设置后会重新生成
  - API 26(Android 8.0/Android O)开始，WidevineId 不再设备唯一，会根据应用包名称不同而不同。 
- **androidId(SSAID, Settings.Secure.ANDROID_ID)** 设备首次启动随机生成的64-bit数字(16进制字符串表示)
  - 需要明确告知用户隐私政策
  - 恢复出厂设置时重新生成
  - API 17(Android 4.2)开始，会对设备上每个用户生成不同的值
  - API 26(Android 8.0/Android O)开始，对于设备上的每个应用和每个用户有不同的值
    - 实际上是应用签名密钥安装时生成 ANDROID_ID
    - 只要软件包名称和签名密钥相同，在软件包卸载或重新安装时，ANDROID_ID 的值不会改变
    - 签名密钥在卸载后应用重新安装时 ANDROID_ID 会改变
  - 少数厂商实现有BUG，导致部分机型上 ANDROID_ID 相同或为null
  - 小米 Android 11 后用户可禁止应用获取 ANDROID_ID
- **instanceId(GUID)** 持久化到内部存储的全局唯一标识符(GUID) 
  - 应用卸载重装或用户清除应用数据后启动时重新生成 
  - iOS 有系统提供 KeyChain 服务可用于存储 GUID，Android 未提供此类服务



widevineId 唯一性问题：[MediaDrm ID 获取重复问题](https://developer.huawei.com/consumer/cn/forum/topic/0209123096367088175)，[Android MediaDrm unique id](https://stackoverflow.com/questions/67396257/android-mediadrm-unique-id)

## 广告标识

同一设备不同应用获取的值相同，可由用户重置或关闭，可用于广告定向与归因。

不需要额外申请权限，但需要明确告知用户隐私政策。

- **开放匿名标识(OAID, Open Anonymous Device Identifier)**   
  https://www.msa-alliance.cn/col.jsp?id=120
- **华为广告标识(OAID, Open Advertising Identifier)**   
  https://developer.huawei.com/consumer/cn/doc/HMSCore-Guides/oaid-0000001050783198
- **Google/Android广告标识(GAID/AAID, Google Advertising ID)**   
  https://support.google.com/googleplay/android-developer/answer/6048248?hl=zh-Hans
- **Apple广告标识(IDFA, ID for advertising)**   



## 硬件标识


同一设备不同应用获取的值相同，不可重置，现在基本不可用了。


- **设备序列号(Build.SERIAL)**，由厂商自定义，一般不可更改，但不具备唯一性
  - 通过读取 品牌、厂商、型号、主板 等硬件信息与设备序列号一起生成一个伪设备标识(Pseudo Device Id)
  - API 26(Android 8.0/Android O)开始，弃用 `Build.SERIAL` 改为 `Build.getSerial()`
- **移动设备标识(IMEI / MEID)**，用于识别物理移动设备，存储在通信基带里的，无通信功能的设备没这个标识
  - [IMEI](https://zh.wikipedia.org/wiki/IMEI) - 国际移动设备识别码(International Mobile Equipment Identity) ，移动设备的唯一标识
  - [MEID](https://zh.wikipedia.org/wiki/MEID) - 移动设备识别码(Mobile Equipment Identifier)，CDMA移动设备的唯一标识，[ESN](https://zh.wikipedia.org/wiki/ESN) 码的升级版
  - 少数厂商上实现有漏洞，会返回空或垃圾信息
- **移动用户信息(IMSI / ICCID)**，存储于 [SIM](https://zh.wikipedia.org/wiki/SIM)卡，若当前用户更换了手机，仍可通过SIM卡的信息跟踪该用户。
  - [IMSI](https://zh.wikipedia.org/wiki/IMSI) - 国际移动用户识别码(International Mobile Subscriber Identification Number)，移动用户的唯一标识
  - [ICCID](https://baike.baidu.com/item/iccid/5181544) - 集成电路卡识别码(Integrate circuit card identity)，SIM卡卡号
- **MAC 地址(Wifi / Bluetooth ...)**，具有全局唯一性，无法由用户重置，在恢复出厂设置后也不会变化。
  - Wifi 权限：`android.permission.ACCESS_WIFI_STATE`
  - Bluetooth 权限：`android.permission.BLUETOOTH`
  - 从API 23(Android 6.0/Android M)开始，系统接口统一返回：02:00:00:00:00:00
  - 从iOS 7.0开始，系统接口统一返回：02:00:00:00:00:00


设备标识符的权限(SERIAL / IMEI / MEID / IMSI / ICCID)

- API 26(Android 8.0/Android O)开始，需要运行时权限 `READ_PHONE_STATE`
- API 29(Android 10/Android Q)开始，需要权限 `READ_PRIVILEGED_PHONE_STATE`，普通应用根本拿不到此权限


```kotlin
TelephonyManager.getDeviceId        // IMEI or MEID
TelephonyManager.getImei            // IMEI
TelephonyManager.getMeid            // MEID
TelephonyManager.getSubscriberId    // IMSI
TelephonyManager.getSubscriberId    // IMSI
TelephonyManager.getSimSerialNumber // ICCID
```




## 参考 

唯一标识符最佳做法    
https://developer.android.com/training/articles/user-data-ids?hl=zh-cn

Android O 中对设备标识符所做的变更   
https://googledeveloperschina.blogspot.com/2017/04/android-o.html

设备标识符   
https://source.android.com/docs/core/connect/device-identifiers?hl=zh-cn
 

对移动设备ID标识合规要求的梳理与思考   
https://www.secrss.com/articles/44392

浅谈设备指纹技术和应用   
https://mp.weixin.qq.com/s/XAGQl8rk4E-JFJYefIHqKg 


漫谈唯一设备ID   
https://juejin.cn/post/6844903952148856839


Android设备唯一标识替代方案（Media Drm ID）   
https://zhuanlan.zhihu.com/p/510133808

使用 MEDIADRM 进行设备 ID 跟踪
https://beltran.work/blog/2018-03-27-device-unique-id-android/



## Gradle

``` groovy
repositories {
    maven { url "https://gitee.com/ezy/repo/raw/cosmo/"}
}
dependencies {
    implementation "me.reezy.cosmo:deviceid:0.8.0"
    implementation "me.reezy.cosmo:deviceid-oaid:0.10.0"
}
```

## LICENSE

The Component is open-sourced software licensed under the [Apache license](LICENSE).