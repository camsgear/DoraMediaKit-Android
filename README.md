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
- [setVideoPath](#setvideopath)
- [initTimeStampView](#initimestampview)
- [isShowTimestamp](#isshowtimestamp)
- [setOnPreparedListener](#setonpreparedlistener)
- [setPerspectiveMode](#setperspectivemode)
- [setMountMode](#setmountmode)
- [setGyroActive](#setgyroactive)
- [resetAngle](#resetangle)
- [start](#start)
- [release](#release)
- [stopPlayback](#stopplayback)
- [stopBackgroundPlay](#stopbackgroundplay)
- [snapshot](#snapshot)
- [startRecordFile](#startRecordFile)
- [stopRecordFile](#stopRecordFile)
- [screenShot](#screenShot)


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
#### setVideoPath
##### 描述:
设置视频流url
##### 函数定义:
```java
public void setVideoPath(String path) 
```
##### 参数说明：
- `path` - 视频流url
##### 使用示例：
  ```java
  String FILE_PATH_H265 = "http://camsgear-demo-resource.oss-cn-hangzhou.aliyuncs.com/h265-test.mp4";
  mDoraVideoView.setVideoPath(FILE_PATH_H265);
  ```
#### initTimeStampView
##### 描述:
初始化时间显示控件，在调用显示时间之前，需要调用此方法；
##### 函数定义:
```java
public void initTimeStampView(int gravity) 
```
##### 参数说明：
- `gravity` - 时间控件显示在视频中的位置
##### 使用示例：
  ```java
  ／／显示在视频的右上方
  mDoraVideoView.initTimeStampView(Gravity.TOP|Gravity.RIGHT);
  ```
#### isShowTimestamp
##### 描述:
是否显示时间控件，在调用此方法之前，需要调用initTimeStampView方法；
##### 函数定义:
```java
public void isShowTimestamp(boolean enable) 
```
##### 参数说明：
- `enable` - 时间控件显示开关
##### 使用示例：
  ```java
  ／／显示时间控件
  mDoraVideoView.isShowTimestamp(true);
  ```
#### setOnPreparedListener
##### 描述:
播放器准备状态监听器
##### 函数定义:
```java
public void setOnPreparedListener(OnPreparedListener listener) 
```
##### 参数说明：
- `listener` - OnPreparedListener监听器
##### 使用示例：
  ```java
  mDoraVideoView.setOnPreparedListener(new IMediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(IMediaPlayer mp) {
                mDoraVideoView.start();//监听器准备好之后，调用；
                ／／Do something
            }
        });
  ```

#### setPerspectiveMode
##### 描述:
设置播放器视图模式
##### 函数定义:
```java
public void setPerspectiveMode(int mode) 
```
##### 参数说明：
- `mode` - 视图模式
```
｛
    PANO_MODE_FISHEYE,
    PANO_MODE_LITTLE_PLANET,
    PANO_MODE_NORMAL 
 ｝
```
##### 使用示例：
  ```java
  //鱼眼视图模式
  mDoraVideoView.setPerspectiveMode(PanoMode.PANO_MODE_FISHEYE);
  ```
  
#### setMountMode
##### 描述:
设置播放器上下倒置模式
##### 函数定义:
```java
public void setMountMode(int mount) 
```
##### 参数说明：
- `mount` - 倒置模式
```
｛
    MOUNT_MODE_DESKTOP,
    MOUNT_MODE_WALL,
    MOUNT_MODE_CEILING
 ｝
```
##### 使用示例：
  ```java
  //倒置模式
  mDoraVideoView.setMountMode(MountMode.MOUNT_MODE_WALL);
  ```
  
#### setGyroActive
##### 描述:
设置陀螺仪开关
##### 函数定义:
```java
public void setGyroActive(boolean isActive) 
```
##### 参数说明：
- `isActive` - 开关
##### 使用示例：
  ```java
  //打开
  mDoraVideoView.setGyroActive(true);
  ```
  
#### resetAngle
##### 描述:
重置视角
##### 函数定义:
```java
public void resetAngle()
```
##### 使用示例：
  ```java
  mDoraVideoView.resetAngle();
  ```
#### start
##### 描述:
开始播放视频,此方法必须在播放器准备好之后调用
##### 函数定义:
```java
public void start()
```
##### 使用示例：
  ```java
  mDoraVideoView.start();
  ```
#### release
##### 描述:
释放资源
##### 函数定义:
```java
public void release()
```
##### 使用示例：
  ```java
  mDoraVideoView.release();
  ```
#### stopPlayback
##### 描述:
停止播放
##### 函数定义:
```java
public void stopPlayback()
```
##### 使用示例：
  ```java
  mDoraVideoView.stopPlayback();
  ```
#### stopBackgroundPlay
##### 描述:
停止后台服务播放
##### 函数定义:
```java
public void stopBackgroundPlay()
```
##### 使用示例：
  ```java
  mDoraVideoView.stopBackgroundPlay();
  ```
#### snapshot
##### 描述:
获取当前视频界面的截图,此方法需要在调用start()之后，出现视频画面之后调用，否则返回空白图片
##### 函数定义:
```java
public Bitmap snapshot()
```
##### 使用示例：
  ```java
  mDoraVideoView.setOnPreparedListener(new IMediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(IMediaPlayer mp) {
                mDoraVideoView.start();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        mDoraVideoView.snapshot();
                    }
                }).start();

            }
        });
  ```
  #### startRecordFile
  ##### 描述:
  录制mp4文件到app
  ##### 函数定义:
  ```java
  public void startRecordFile(String fileName)
  ```
  ##### 参数说明：
  - `fileName` - 要保存的文件完整路径
  ##### 使用示例：
    ```java
        String path = Environment.getExternalStorageDirectory() + "/" + "Download" + "/";
        File folder = new File(path);
        if (!folder.exists() && !folder.mkdirs()) {
            return;
        }
        long dataTake = System.currentTimeMillis();
        final String name  = path + dataTake + ".mp4";
        mDoraVideoView.startRecordFile(name);
    ```
  #### stopRecordFile
  ##### 描述:
  stop mp4 record
  ##### 函数定义:
  ```java
  public void stopRecordFile()
  ```
  ##### 使用示例：
    ```java
       mDoraVideoView.stopRecordFile();
    ```
  #### screenShot
  ##### 描述:
  录制jpg文件到app
  ##### 函数定义:
  ```java
  public void screenShot(String fileName)
  ```
  ##### 参数说明：
  - `fileName` - 要保存的文件完整路径
  ##### 使用示例：
  ```java
        String path = Environment.getExternalStorageDirectory() + "/" + "Download" + "/";
        File folder = new File(path);
        if (!folder.exists() && !folder.mkdirs()) {
            return;
        }
        long dataTake = System.currentTimeMillis();
        final String name  = path + dataTake + ".jpg";
        mDoraVideoView.screenShot(name);
  ```