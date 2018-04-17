# DoraMediaKit-Android
## 配置
* 1.在项目的gradle文件中增加引用：
```gradle
allprojects {
  repositories {
       maven {
            url "http://maven.camsgear.com/artifactory/gradle-release-local"
            credentials {
                username 'YOUR_USERNAME'
                password 'YOUR_PASSWORD'
            }
        }
    ｝
}
```
* 2.在app 的gradle文件中增加引用：
```gradle

dependencies {
    compile 'com.camsgear.doramediakit:doramediakit:1.0.0'
}
```
* 3.在AndroidManifest.xml文件中增加权限：
```xml
  <uses-permission android:name="android.permission.INTERNET" />
  <uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />
  <uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE" />
```
* 4.在Activity里面需要加载so库文件：
```java
 static {
        DoraMediaPlayer.loadLibrariesOnce(null);
        DoraMediaPlayer.native_profileBegin("libdoraplayer.so");
    }
```
* 5.在Activity的onDestroy里面需要加：
```java
 DoraMediaPlayer.native_profileEnd();
```
## 函数说明
- [播放器](#doravideoview)
## DoraVideoView
DoraVideoView.java 图片列表接口
### 接口列表
- [setOptionCategory](#setoptioncategory)
- [setCalibrationData](#setcalibrationdata)
- [setMediaInfo](#setmediainfo)

#### setOptionCategory
##### 描述:
设置播放器播放的视频流类别
##### 函数定义:
```java
public void setOptionCategory(PlayerOptionCategory category)
```
##### 参数说明：
- `category` - PlayerOptionCategory有四种流类型
```
｛
    CATEGORY_NORMAL（一般类型）
    CATEGORY_RTSP（RTSP流）
    CATEGORY_RTMP（RTMP流）
    CATEGORY_P2P(视频流通过Ｐ２Ｐ)
  ｝
```
##### 使用示例：
  ```java
  PlayerOptionCategory mPlayerOption = PlayerOptionCategory.CATEGORY_NORMAL;
  mDoraVideoView.setOptionCategory(mPlayerOption);
  ```
#### setCalibrationData
##### 描述:
设置标定参数
##### 函数定义:
```java
public void setCalibrationData(String data) 
```
##### 参数说明：
- `data` - 双鱼眼图片的标定拼接参数
##### 使用示例：
  ```java
  mDoraVideoView.setCalibrationData(GlobalDefine.CALIBRATION_DOUBLE_FISHEYE_265);
  ```
#### setMediaInfo
##### 描述:
设置媒体参数
##### 函数定义:
```java
public void setMediaInfo(String mediaInfoString) 
```
##### 参数说明：
- `mediaInfoString` - 设置播放器的媒体参数
##### 使用示例：
  ```java
  mDoraVideoView.setMediaInfo(MediaInfoUtil.getDoubleMediaInfoBean());
  ```
